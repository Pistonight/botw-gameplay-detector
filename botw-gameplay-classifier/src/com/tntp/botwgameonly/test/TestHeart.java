package com.tntp.botwgameonly.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tntp.botwgameonly.classifier.HeartClassifier;
import com.tntp.botwgameonly.data.ClassifiableImage;
import com.tntp.botwgameonly.data.HeartCleaner;

public class TestHeart {
    public static void main(String[] args) throws IOException {
        BufferedImage input = ImageIO.read(new File("heart1.png"));
        ClassifiableImage image = new ClassifiableImage(input, new HeartClassifier(), new HeartCleaner());
        System.out.println(image.classify());
    }
}
