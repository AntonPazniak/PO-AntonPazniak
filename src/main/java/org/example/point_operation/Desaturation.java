package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Desaturation {

    public static void convert(PortableAnymap portableAnymap) {
        if (portableAnymap.getHead().equals("P3") || portableAnymap.getHead().equals("P6")) {
            portableAnymap.setMatrix(convertP3(portableAnymap.getMatrix()));
            portableAnymap.updateImage();
        }
    }


    public static int[][][] convertP3(int[][][] matrix) {
        int[][][] newMatrix = new int[matrix.length][matrix[0].length][matrix[0][0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int R = matrix[i][j][0];
                int G = matrix[i][j][1];
                int B = matrix[i][j][2];
                int Y = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                newMatrix[i][j] = new int[]{Y, Y, Y};
            }
        }
        return newMatrix;
    }

    public static int[][] convertToGrayscale2D(int[][][] rgb) {
        int height = rgb.length;
        int width = rgb[0].length;
        int[][] grayscale = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int r = rgb[i][j][0];
                int g = rgb[i][j][1];
                int b = rgb[i][j][2];
                grayscale[i][j] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
            }
        }
        return grayscale;
    }
}