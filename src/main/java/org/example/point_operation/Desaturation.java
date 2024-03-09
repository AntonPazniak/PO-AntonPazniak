package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;

public class Desaturation {

    public static void convert(PortableAnymap portableAnymap) {
        if (portableAnymap.getHead().equals("P3")) {
            convertP3(portableAnymap);
        }
    }

    public static void convertP3(PortableAnymap portableAnymap) {
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                portableAnymap.getMatrix()[i][j][1] = portableAnymap.getMatrix()[i][j][0];
                portableAnymap.getMatrix()[i][j][2] = portableAnymap.getMatrix()[i][j][0];
            }
        }
        portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(portableAnymap.getMatrix()));
    }
}