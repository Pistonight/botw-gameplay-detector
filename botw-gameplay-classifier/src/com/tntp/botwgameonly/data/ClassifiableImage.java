package com.tntp.botwgameonly.data;

import java.awt.image.BufferedImage;
import java.util.BitSet;

import com.tntp.botwgameonly.classifier.Classifier;

public final class ClassifiableImage {
    private BufferedImage image;
    private Classifier classifier;
    private ImageCleaner cleaner;

    public ClassifiableImage(BufferedImage image, Classifier classifier, ImageCleaner cleaner) {
        this.image = image;
        this.classifier = classifier;
        this.cleaner = cleaner;
    }

    public boolean classify() {
        boolean[][] cleanedData = cleaner.clean(image);
        BitSet bitset = convertToBitSet(cleanedData);
        return classifier.classify(bitset);
    }

    private BitSet convertToBitSet(boolean[][] cleanedArray) {
        BitSet data = new BitSet();
        int w = image.getWidth();
        int h = image.getHeight();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int i = x * w + y;
                if (cleanedArray[x][y]) {
                    data.set(i);
                }
            }
        }
        return data;
    }
}
