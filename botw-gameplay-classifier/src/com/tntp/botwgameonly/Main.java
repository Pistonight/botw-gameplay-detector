package com.tntp.botwgameonly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.tntp.botwgameonly.processor.Classify;
import com.tntp.botwgameonly.processor.Crop;
import com.tntp.botwgameonly.video.VideoProcessor;

public class Main {
    public static int HEART_THRESHOLD = 130;
    public static int HEART_PIXEL_THRESHOLD = 50;
    public static int SKIP_BUTTON_THRESHOLD = 120;
    public static int SKIP_BUTTON_PIXEL_THRESHOLD = 75;

    public static int MIN_INTERVAL = 10;
    public static int STREAK = 15;

    public static void main(String[] args) throws IOException {

        String inFile = args[0];
        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int w = Integer.parseInt(args[3]);
        int h = Integer.parseInt(args[4]);
        Main.HEART_THRESHOLD = Integer.parseInt(args[5]);
        Main.SKIP_BUTTON_THRESHOLD = Integer.parseInt(args[6]);
        Main.HEART_PIXEL_THRESHOLD = Integer.parseInt(args[7]);
        Main.SKIP_BUTTON_PIXEL_THRESHOLD = Integer.parseInt(args[8]);
        Main.MIN_INTERVAL = Integer.parseInt(args[9]);
        Main.STREAK = Integer.parseInt(args[10]);
        String outFile = args[11];

        
        execute(inFile, outFile, x, y, w, h);
        
    }


    private static void execute(String inFile, String outFile_, int x, int y, int w, int h) {
        Crop frameProcessor = new Crop(new Classify());
        // frameProcessor.setBounds(333, 64, 1587, 892);
        frameProcessor.setBounds(x, y, w, h);
        VideoProcessor processor = null;
        try {
            processor = new VideoProcessor(inFile, frameProcessor);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        LogThread log = new LogThread(processor);
        log.start();
        double[] times = processor.process();
        log.interrupt();
        try {
            log.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File outFile = new File(outFile_);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        pw.println(Arrays.toString(times));
        pw.flush();
        pw.close();
    }
}
