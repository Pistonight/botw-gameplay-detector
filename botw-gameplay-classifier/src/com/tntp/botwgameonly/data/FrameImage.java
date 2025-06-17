package com.tntp.botwgameonly.data;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.tntp.botwgameonly.classifier.HeartClassifier;
import com.tntp.botwgameonly.classifier.SkipButtonClassifier;

public class FrameImage {
    private BufferedImage frame;

    public FrameImage(BufferedImage image) {
        frame = image;
    }

    public boolean isCutscene() {
        return !hasThreeHearts() || hasSkipButton();
    }

    public boolean hasThreeHearts() {
        for (int i = 0; i < 3; i++) {
            if (hasHeartContainer(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHeartContainer(int index) {
        BufferedImage heart = cropHeartPosition(index);
        ClassifiableImage image = new ClassifiableImage(heart, new HeartClassifier(), new HeartCleaner());
        return image.classify();
    }

    private BufferedImage cropHeartPosition(int index) {
        final int X = 84, Y = 45, W = 30, H = 30;
        float scale = frame.getWidth() / 1920f;
        int scaledX = Math.round((X + (index * W)) * scale);
        int scaledY = Math.round(Y * scale);
        int scaledW = Math.round(W * scale);
        int scaledH = Math.round(H * scale);
        BufferedImage cropped = frame.getSubimage(scaledX, scaledY, scaledW, scaledH);
        BufferedImage scaledImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) scaledImage.getGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.addRenderingHints(rh);
        g.drawImage(cropped, 0, 0, W, H, 0, 0, scaledW, scaledH, null);
        return scaledImage;
    }

    public boolean hasSkipButton() {
        BufferedImage skipButton = cropSkipButtonPosition();
        ClassifiableImage image = new ClassifiableImage(skipButton, new SkipButtonClassifier(), new SkipButtonCleaner());
        return image.classify();
    }

    private BufferedImage cropSkipButtonPosition() {
        final int X = 1785, Y = 1002, SIZE = 30;
        float scale = frame.getWidth() / 1920f;
        int scaledX = Math.round(X * scale);
        int scaledY = Math.round(Y * scale);
        int scaledSize = Math.round(SIZE * scale);
        BufferedImage cropped = frame.getSubimage(scaledX, scaledY, scaledSize, scaledSize);
        BufferedImage scaledImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) scaledImage.getGraphics();
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.addRenderingHints(rh);
        g.drawImage(cropped, 0, 0, SIZE, SIZE, 0, 0, scaledSize, scaledSize, null);
        return scaledImage;
    }
}
