package com.tntp.botwgameonly.data;

import java.awt.image.BufferedImage;

import com.tntp.botwgameonly.Main;

public class SkipButtonCleaner implements ImageCleaner {

    @Override
    public boolean[][] clean(BufferedImage image) {
        boolean[][] data = new boolean[image.getWidth()][image.getHeight()];
        PixelComparator pComp = new PixelComparator(Main.SKIP_BUTTON_PIXEL_THRESHOLD);
        ImagePolarizer polarizer = new ImagePolarizer(data, image, pComp);
        polarizer.setBounds(0, 30, 0, 30);
        polarizer.setTarget(7, 14);
        polarizer.polarize();
        return data;
    }

}
