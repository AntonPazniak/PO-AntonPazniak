package org.example.pars;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class CreateImageFromMatrix {

    public static Image createImageFromRGBMatrix(int[][][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = new Color(getColorValue(rgbMatrix[y][x])).getRGB();
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }


    private static int getGrayColorValue(int color) {
        return (color << 16) | (color << 8) | color;
    }
    public static int getColorValue(int [] colors) {
        return (colors[0] << 16) | (colors[1] << 8) | colors[2];
    }
    private static int[] getRGBValues(int colorValue) {
        int red = (colorValue >> 16) & 0xFF;
        int green = (colorValue >> 8) & 0xFF;
        int blue = colorValue & 0xFF;

        return new int[]{red, green, blue};
    }
}
