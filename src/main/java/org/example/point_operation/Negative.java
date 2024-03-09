package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;


public class Negative {

    public static void convert(PortableAnymap portableAnymap) {
        switch (portableAnymap.getHead()) {
            case "P1" -> convertP1(portableAnymap);
            case "P2" -> convertP2(portableAnymap);
            case "P3" -> convertP3(portableAnymap);
        }
    }

    private static void convertP1(PortableAnymap portableAnymap) {
        int[][] newMatrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                newMatrix[i][j] = 1 - portableAnymap.getMatrix()[i][j];
            }
        }
        portableAnymap.setMatrix(newMatrix);
        portableAnymap.setImage(CreateImageFromMatrix.createPBMImageFromBinaryMatrix(newMatrix));
    }

    private static void convertP2(PortableAnymap portableAnymap) {
        int[][] newMatrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                newMatrix[i][j] = portableAnymap.getColor() - portableAnymap.getMatrix()[i][j];
            }
        }
        portableAnymap.setMatrix(newMatrix);
        portableAnymap.setImage(CreateImageFromMatrix.createPPMImageFromRGBMatrix(newMatrix));
    }

    private static void convertP3(PortableAnymap portableAnymap) {
        int[][] newMatrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        int maxColor = portableAnymap.getColor() * 65536 + portableAnymap.getColor() * 256 + portableAnymap.getColor();
        System.out.println(maxColor);
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                newMatrix[i][j] = maxColor - portableAnymap.getMatrix()[i][j];
            }
        }
        portableAnymap.setMatrix(newMatrix);
        portableAnymap.setImage(CreateImageFromMatrix.createPPMImageFromRGBMatrix(newMatrix));
    }
}