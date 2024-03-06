package org.example.pars;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CreateImageFromMatrix {

    public static BufferedImage createPGMImageFromRGBMatrix(int[][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = rgbMatrix[y][x];
                int grayscale = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                int invertedGray = 255 - grayscale;
                int invertedGrayPixel = (invertedGray << 16) | (invertedGray << 8) | invertedGray;
                image.setRGB(x, y, invertedGrayPixel);
            }
        }

        return image;
    }


    public static Image createImageFromRGBMatrix(int[][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = new Color(rgbMatrix[y][x]).getRGB();
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

    public static BufferedImage createPBMImageFromBinaryMatrix(int[][] binaryMatrix) {
        int width = binaryMatrix[0].length;
        int height = binaryMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = (binaryMatrix[y][x] == 0) ? 0xFFFFFF : 0x000000;
                image.setRGB(x, y, pixelValue);
            }
        }

        return image;
    }

}
