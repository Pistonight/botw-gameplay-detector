package com.tntp.botwgameonly.processor;

import java.awt.image.BufferedImage;

public interface FrameProcessor {
    boolean shouldInclude(BufferedImage rawFrame);
}
