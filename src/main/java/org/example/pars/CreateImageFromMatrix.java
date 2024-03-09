package org.example.pars;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class CreateImageFromMatrix {

    public static BufferedImage createPPMImageFromRGBMatrix(int[][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = getGrayColorValue(rgbMatrix[y][x]);
                image.setRGB(x, y, rgb);
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


    private static int getGrayColorValue(int color) {
        return (color << 16) | (color << 8) | color;
    }
    public static int getColorValue(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }
    private static int[] getRGBValues(int colorValue) {
        int red = (colorValue >> 16) & 0xFF;
        int green = (colorValue >> 8) & 0xFF;
        int blue = colorValue & 0xFF;

        return new int[]{red, green, blue};
    }
}
