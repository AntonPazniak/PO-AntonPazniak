package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.Slider;
import org.example.window.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Contrast {


    public static void convert(PortableAnymap portableAnymap, Window window) {
        Slider slider = new Slider("Test", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
                float value = (slider.getSlider().getValue());
                if (value > 0) {
                    value = value / 10;
                } else {
                    value = Math.abs(value / 100 - 1);
                }
                increaseContrast(workMatrix, value);
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                window.resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int value = (slider.getSlider().getValue() / 10);
                increaseContrast(portableAnymap.getMatrix(), value);
                portableAnymap.updateImage();
                window.resizeImage();
                slider.getFrame().dispose();
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
