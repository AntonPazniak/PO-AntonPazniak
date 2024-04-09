package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;


public class Negative implements PointFilter {

    @Override
    public void convert(PortableAnymap portableAnymap) {
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