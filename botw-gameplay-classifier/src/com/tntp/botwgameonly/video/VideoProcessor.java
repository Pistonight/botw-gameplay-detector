package com.tntp.botwgameonly.video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.tntp.botwgameonly.processor.FrameProcessor;
import com.tntp.botwgameonly.Main;

public class VideoProcessor {
    private VideoInput input;
    private FrameProcessor processor;
    private volatile int progress;
    private List<Integer> framePoints;

    public VideoProcessor(String inputName, FrameProcessor processor) throws IOException {
        framePoints = new ArrayList<>();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        if (!Paths.get(inputName).toFile().exists()) {
            throw new FileNotFoundException();
        }

        VideoCapture camera = new VideoCapture(inputName);

        if (!camera.isOpened()) {
            throw new IOException("Cannot open video");
        }
        input = new VideoInput(camera);
        this.processor = processor;
    }

    public double[] process() {
        double fps = input.getFPS();
        System.out.println(fps);
        final int STREAK = Main.STREAK;
        final int MIN_REMOVE_LENGTH = Main.MIN_INTERVAL;
        Mat mat = new Mat();
        int excludeStreak = 0;
        int includeStreak = 0;
        boolean currentShouldInclude = false;
        int count = 0;
        while (input.readNextFrame(mat)) {
            BufferedImage img = matToBufferedImage(mat);
            boolean include = processor.shouldInclude(img);
            if (include) {
                excludeStreak = 0;
                if (!currentShouldInclude) {
                    includeStreak++;
                    if (includeStreak >= STREAK) {
                        int frameStart = count - STREAK - 1;
                        if (!framePoints.isEmpty()) {
                            int lastEnd = framePoints.get(framePoints.size() - 1);
                            if (frameStart - lastEnd < MIN_REMOVE_LENGTH) {
                                framePoints.remove(framePoints.size() - 1);
                               // System.out.println("REMOVE END: " + lastEnd);
                            } else {
                                framePoints.add(frameStart);
                               // System.out.println("Found Start: " + frameStart);
                            }
                        } else {
                            framePoints.add(frameStart);
                           // System.out.println("Found Start: " + frameStart);
                        }
                        currentShouldInclude = true;
                        includeStreak = 0;
                    }

                }
            } else {
                includeStreak = 0;
                if (currentShouldInclude) {
                    excludeStreak++;
                    if (excludeStreak >= STREAK) {
                        int frameEnd = count - STREAK + 1;
                        framePoints.add(frameEnd);
                       // System.out.println("Found End: " + frameEnd);
                        currentShouldInclude = false;
                        excludeStreak = 0;
                    }
                }
            }
            count++;
            progress++;
        }
        input.done();
        if (currentShouldInclude) {
            int frameEnd = count - 1;
            framePoints.add(frameEnd);
           // System.out.println("Include End: " + frameEnd);
        }
        double[] times = new double[framePoints.size()];

        for (int i = 0; i < times.length; i++) {
            times[i] = framePoints.get(i) / fps;
        }
        return times;
    }

    public int getTotalFrames() {
        return input.totalFrames();
    }

    public int getProgress() {
        return progress;
    }

    private BufferedImage matToBufferedImage(Mat frame) {
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }
}
