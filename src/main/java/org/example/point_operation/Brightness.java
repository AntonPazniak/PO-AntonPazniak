package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Brightness {


    public static void convert(PortableAnymap image, double factor) {
        increaseContrast(image.getMatrix(), factor);
    }

    public static void increaseContrast(int[][][] matrix, double gamma) {
        int width = matrix.length;
        int height = matrix[0].length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = matrix[x][y][0];
                int green = matrix[x][y][1];
                int blue = matrix[x][y][2];

                int correctedRed = (int) (255 * Math.pow((double) red / 255, gamma));
                int correctedGreen = (int) (255 * Math.pow((double) green / 255, gamma));
                int correctedBlue = (int) (255 * Math.pow((double) blue / 255, gamma));

                matrix[x][y][0] = correctedRed;
                matrix[x][y][1] = correctedGreen;
                matrix[x][y][2] = correctedBlue;
            }
        }
    }

}
