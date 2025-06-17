package com.tntp.botwgameonly.data;

import java.awt.image.BufferedImage;

import com.tntp.botwgameonly.Main;

public class HeartCleaner implements ImageCleaner {

    @Override
    public boolean[][] clean(BufferedImage image) {
//        boolean[][] data = new boolean[input.getWidth()][input.getHeight()];
//        ImagePolarizer polarizer = new ImagePolarizer(data, input, new PixelComparator(40));
//        cleanTopLeft(polarizer);
//        cleanBottomLeft(polarizer);
//        cleanBottomRight(polarizer);
//        cleanTopRight(polarizer);
//        return data;
        boolean[][] data = new boolean[image.getWidth()][image.getHeight()];
        PixelComparator pComp = new PixelComparator(Main.HEART_PIXEL_THRESHOLD);
        ImagePolarizer polarizer = new ImagePolarizer(data, image, pComp);
        polarizer.setBounds(0, 30, 0, 30);
        polarizer.setTarget(8, 11);
        polarizer.polarize();
        return data;
    }

//    private void cleanTopLeft(ImagePolarizer polarizer) {
//        polarizer.setBounds(0, 12, 0, 10);
//        polarizer.setTarget(9, 7);
//        polarizer.polarize();
//    }
//
//    private void cleanBottomLeft(ImagePolarizer polarizer) {
//        polarizer.setBounds(0, 12, 10, 10);
//        polarizer.setTarget(9, 12);
//        polarizer.polarize();
//    }
//
//    private void cleanBottomRight(ImagePolarizer polarizer) {
//        polarizer.setBounds(12, 12, 10, 10);
//        polarizer.setTarget(14, 12);
//        polarizer.polarize();
//    }
//
//    private void cleanTopRight(ImagePolarizer polarizer) {
//        polarizer.setBounds(12, 12, 0, 10);
//        polarizer.setTarget(14, 7);
//        polarizer.polarize();
//    }

}
