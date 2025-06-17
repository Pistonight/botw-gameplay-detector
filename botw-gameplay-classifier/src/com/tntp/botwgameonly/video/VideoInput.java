package com.tntp.botwgameonly.video;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class VideoInput {

    private VideoCapture camera;

    public VideoInput(VideoCapture camera) {
        this.camera = camera;
    }

    public boolean readNextFrame(Mat frame) {
        return camera.read(frame);
    }

    public int totalFrames() {
        return (int) camera.get(Videoio.CAP_PROP_FRAME_COUNT);
    }

    public void done() {
        camera.release();
    }

    public double getFPS() {
        return camera.get(Videoio.CAP_PROP_FPS);
    }

}
