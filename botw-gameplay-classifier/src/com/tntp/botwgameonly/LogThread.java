package com.tntp.botwgameonly;

import com.tntp.botwgameonly.video.VideoProcessor;

public class LogThread extends Thread {
    private VideoProcessor processor;


    public LogThread(VideoProcessor processor) {
        this.processor = processor;

    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                break;
            }
            System.out.println("Progress: " + processor.getProgress() + "/" + processor.getTotalFrames());
        }
        System.out.println();
    }
}
