package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.Slider;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Contrast {

    public static void convert(PortableAnymap portableAnymap) {
        Slider slider = new Slider("Test", 100, -100, 0);
        slider.getImageLabel().setIcon(new ImageIcon(portableAnymap.getImage()));
        int[][][] newMatrix = portableAnymap.getMatrix();
        slider.getSlider().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = (slider.getSlider().getValue() + 100);
                int[][][] workMatrix = increaseContrast(portableAnymap.getMatrix(), value);
                portableAnymap.getImageLabel().setIcon(new ImageIcon(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix)));
            }
        });
    }

    private static int getAverageMatrix(int[][][] matrix) {
        int average = 0;
        for (int[][] i : matrix) {
            for (int[] j : i) {
                average = average + j[2];
            }
        }
        return average / matrix.length / 3;
    }

    private static void convertP2P3(PortableAnymap portableAnymap) {
        portableAnymap.setMatrix(increaseContrast(portableAnymap.getMatrix(), 1.2));
        portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(portableAnymap.getMatrix()));
    }

    private static int[][][] increaseContrast(int[][][] imageMatrix, double factor) {
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
