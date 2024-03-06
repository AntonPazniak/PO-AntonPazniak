package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.Convert;
import org.example.pars.CreateImageFromMatrix;

public class Desaturation {

    public static void convert(PortableAnymap portableAnymap) {
        if (portableAnymap.getHead().equals("P2")) {
            convertP2(portableAnymap);
        } else if (portableAnymap.getHead().equals("P3")) {
            convertP3(portableAnymap);
        }
    }


    private static void convertP2(PortableAnymap portableAnymap) {

    }

    public static void convertP3(PortableAnymap portableAnymap) {
        int [][] newMatrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        for (int i = 0; i < portableAnymap.getHeight();i++){
            for(int j = 0; j<portableAnymap.getWidth();j++){
                int[] colors = Convert.getRGBValues(portableAnymap.getMatrix()[i][j]);
                newMatrix[i][j] = (int) (0.299 * colors[1] + 0.587 * colors[2] + 0.114 * colors[2]);

            }
        }
        portableAnymap.setMatrix(newMatrix);
        portableAnymap.setImage(CreateImageFromMatrix.createPGMImageFromRGBMatrix(newMatrix));
    }
}
