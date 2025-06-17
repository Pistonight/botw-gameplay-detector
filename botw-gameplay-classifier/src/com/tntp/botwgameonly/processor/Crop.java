package com.tntp.botwgameonly.processor;

import java.awt.image.BufferedImage;

public class Crop implements FrameProcessor {
    private int x;
    private int y;
    private int w;
    private int h;
    private FrameProcessor delegate;

    public Crop(FrameProcessor delegate) {
        this.delegate = delegate;
    }

    public void setBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public boolean shouldInclude(BufferedImage rawFrame) {
        return delegate.shouldInclude(rawFrame.getSubimage(x, y, w, h));
    }

}
