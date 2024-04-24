package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Contrast {


    public static void convert(PortableAnymap portableAnymap, double factor) {
        portableAnymap.setMatrix(increaseContrast(portableAnymap.getMatrix(), factor));
    }

    public static int[][][] increaseContrast(int[][][] imageMatrix, double factor) {
        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                for (int k = 0; k < imageMatrix[i][j].length; k++) {
                    double pixelValue = imageMatrix[i][j][k];
                    double adjustedPixel = (pixelValue - 128) * factor + 128;
                    imageMatrix[i][j][k] = (int) Math.round(Math.min(Math.max(adjustedPixel, 0), 255));
                }
            }
        }
        return imageMatrix;
    }


}
