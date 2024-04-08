package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.MainWindow;
import org.example.window.SliderWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Contrast {


    public static void convert(PortableAnymap portableAnymap, MainWindow window) {
        SliderWindow slider = new SliderWindow("Test", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
                increaseContrast(workMatrix,
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                window.resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseContrast(portableAnymap.getMatrix(),
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.updateImage();
                window.resizeImage();
                slider.dispose();
            }
        });

    }

    private static float getFactor(float factor) {
        if (factor > 0) {
            factor = factor / 10;
        } else {
            factor = Math.abs(factor / 100 + 1);
        }
        return factor;
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
