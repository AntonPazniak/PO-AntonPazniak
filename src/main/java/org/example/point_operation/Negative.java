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
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                int color = 255 -portableAnymap.getMatrix()[i][j][2];
                portableAnymap.getMatrix()[i][j] = new int [] {color,color,color};
            }
        }
        portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(portableAnymap.getMatrix()));
    }

    private static void convertP2(PortableAnymap portableAnymap) {
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                int color = portableAnymap.getColor() - portableAnymap.getMatrix()[i][j][0];
                portableAnymap.getMatrix()[i][j] = new int[] {color,color,color};
            }
        }
        portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(portableAnymap.getMatrix()));
    }

    private static void convertP3(PortableAnymap portableAnymap) {
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                portableAnymap.getMatrix()[i][j] = new int[] {
                        portableAnymap.getColor() - portableAnymap.getMatrix()[i][j][0],
                        portableAnymap.getColor() - portableAnymap.getMatrix()[i][j][1],
                        portableAnymap.getColor() - portableAnymap.getMatrix()[i][j][2]};
            }
        }
        portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(portableAnymap.getMatrix()));
    }
}