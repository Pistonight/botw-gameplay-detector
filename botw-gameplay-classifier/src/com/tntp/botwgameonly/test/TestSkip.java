package com.tntp.botwgameonly.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tntp.botwgameonly.classifier.SkipButtonClassifier;
import com.tntp.botwgameonly.data.ClassifiableImage;
import com.tntp.botwgameonly.data.SkipButtonCleaner;

public class TestSkip {
    public static void main(String[] args) throws IOException {
        BufferedImage input = ImageIO.read(new File("skip1.png"));
        ClassifiableImage image = new ClassifiableImage(input, new SkipButtonClassifier(), new SkipButtonCleaner());
        System.out.println(image.classify());
    }
}
