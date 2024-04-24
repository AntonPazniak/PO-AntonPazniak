package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Brightness {


    public static void convert(PortableAnymap image, double factor) {
        increaseContrast(image.getMatrix(), factor);
    }

    public static void increaseContrast(int[][][] imageMatrix, double factor) {
        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                for (int k = 0; k < imageMatrix[i][j].length; k++) {
                    int color = imageMatrix[i][j][k];
                    color = Math.min(Math.max((int) (color + color * factor), 0), 255);
                    imageMatrix[i][j][k] = color;
                }
            }
        }
    }
}
