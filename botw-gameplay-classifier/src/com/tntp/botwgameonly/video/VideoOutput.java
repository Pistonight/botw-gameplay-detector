package com.tntp.botwgameonly.video;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

public class VideoOutput {
    private VideoWriter writer;

    public VideoOutput(String outputName, VideoCapture input) {
        Size frameSize = new Size((int) input.get(Videoio.CAP_PROP_FRAME_WIDTH), (int) input.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        writer = new VideoWriter(outputName, VideoWriter.fourcc('h', '2', '6', '4'), input.get(Videoio.CAP_PROP_FPS), frameSize, true);
    }

    public void writeFrame(Mat frame) {
        writer.write(frame);
    }

    public void done() {
        writer.release();
    }
}
