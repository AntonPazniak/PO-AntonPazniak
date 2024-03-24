package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Desaturation {

    public static void convert(PortableAnymap portableAnymap) {
        if (portableAnymap.getHead().equals("P3")) {
            convertP3(portableAnymap.getMatrix());
            portableAnymap.updateImage();
        }
    }


    public static void convertP3(int[][][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                int R = matrix[i][j][0];
                int G = matrix[i][j][1];
                int B = matrix[i][j][2];
                int Y = (int) (0.2126 * R + 0.7152 * G + 0.0722 * B);
                matrix[i][j] = new int[]{Y, Y, Y};
            }
        }
    }
}