package com.tntp.botwgameonly.processor;

import java.awt.image.BufferedImage;

import com.tntp.botwgameonly.data.FrameImage;

public class Classify implements FrameProcessor {

    @Override
    public boolean shouldInclude(BufferedImage rawFrame) {
        FrameImage frame = new FrameImage(rawFrame);
        return !frame.isCutscene();
    }

}
