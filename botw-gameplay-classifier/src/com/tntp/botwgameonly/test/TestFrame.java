package com.tntp.botwgameonly.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.tntp.botwgameonly.data.FrameImage;

public class TestFrame {
    public static void main(String[] args) throws IOException {
        BufferedImage input = ImageIO.read(new File("hearttest.jpg"));
        FrameImage frame = new FrameImage(input);
        System.out.println(frame.hasHeartContainer(0));
        System.out.println(frame.hasHeartContainer(1));
        System.out.println(frame.hasHeartContainer(2));
        System.out.println(frame.hasHeartContainer(3));
        System.out.println(frame.hasHeartContainer(15));
        System.out.println(frame.hasSkipButton());
    }
}
