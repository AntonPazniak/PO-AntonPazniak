package org.example.point_operation;

import org.example.models.PortableAnymap;

public class SDP {


    public static void sum(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        matrix[i][j][l] = Math.min(255,
                                Math.max(0, matrix[i][j][l] + portableAnymap1.getMatrix()[i][j][l]));
                    }
                }
            }
        }
    }


    public static void dif(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        matrix[i][j][l] = Math.min(255,
                                Math.max(0, matrix[i][j][l] - portableAnymap1.getMatrix()[i][j][l]));
                    }
                }
            }
        }
    }

    public static void product(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        int pixel = portableAnymap.getMatrix()[i][j][l] * (portableAnymap1.getMatrix()[i][j][l] / 255);
                        matrix[i][j][l] = pixel;
                    }
                }
            }
        }
    }
}
