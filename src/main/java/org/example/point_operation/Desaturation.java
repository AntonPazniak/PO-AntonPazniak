package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Desaturation implements PointFilter {

    @Override
    public void convert(PortableAnymap portableAnymap) {
        if (portableAnymap.getHead().equals("P3")) {
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
                int Y = (int) (0.2126 * R + 0.7152 * G + 0.0722 * B);
                newMatrix[i][j] = new int[]{Y, Y, Y};
            }
        }
        return newMatrix;
    }
}