package com.tntp.botwgameonly.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImagePolarizer {
    private PixelComparator pComp;
    private boolean[][] output;
    private BufferedImage image;
    private int xStart;
    private int xLength;
    private int yStart;
    private int yLength;
    private int xTarget;
    private int yTarget;

    public ImagePolarizer(boolean[][] output, BufferedImage image, PixelComparator pComp) {
        this.output = output;
        this.image = image;
        this.pComp = pComp;
    }

    public void setBounds(int xStart, int xLength, int yStart, int yLength) {
        this.xStart = xStart;
        this.xLength = xLength;
        this.yStart = yStart;
        this.yLength = yLength;
    }

    public void setTarget(int x, int y) {
        xTarget = x;
        yTarget = y;
    }

    public void polarize() {
        boolean[][] processed = new boolean[xLength][yLength];
        Color color = new Color(image.getRGB(xTarget, yTarget));
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        pComp.setTarget(r, g, b);
        polarizeAt(xTarget, yTarget, processed);
    }

    private void polarizeAt(int x, int y, boolean[][] processed) {
        if (x < xStart || x >= xStart + xLength) {
            return;
        }
        if (y < yStart || y >= yStart + yLength) {
            return;
        }
        if (processed[x - xStart][y - yStart]) {
            return;
        }
        processed[x - xStart][y - yStart] = true;
        Color color = new Color(image.getRGB(x, y));
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        boolean similar = pComp.isSimilar(r, g, b);
        output[x][y] = similar;
        if (similar) {
            polarizeAt(x - 1, y, processed);
            polarizeAt(x + 1, y, processed);
            polarizeAt(x, y - 1, processed);
            polarizeAt(x, y + 1, processed);
        }
    }
}
