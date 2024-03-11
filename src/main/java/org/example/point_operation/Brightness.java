package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.Slider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Brightness {
    private static int[][][] newMatrix;

    public static void convert(PortableAnymap portableAnymap) {
        Slider slider = new Slider("Brightness", 100, -100, 0);
        newMatrix = copyMatrix(portableAnymap.getMatrix());
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                System.out.println(luminosity);
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(increaseContrast(newMatrix, luminosity)));
                portableAnymap.getImageLabel().setIcon(new ImageIcon(portableAnymap.getImage()));
            }
        });
    }

    private static int[][][] increaseContrast(int[][][] imageMatrix, double factor) {
        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                for (int k = 0; k < imageMatrix[i][j].length; k++) {
                    int color = imageMatrix[i][j][k];
                    color = (int) (color + color * factor);
                    if (color > 250)
                        color = 250;
                    else if (color < 0)
                        color = 0;
                    imageMatrix[i][j][k] = color;
                }
            }
        }
        return imageMatrix;
    }

    private static int[][][] copyMatrix(int[][][] matrix) {
        int[][][] copy = new int[matrix.length][][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = new int[matrix[i].length][];
            for (int j = 0; j < matrix[i].length; j++) {
                copy[i][j] = matrix[i][j].clone();
            }
        }
        return copy;
    }

}
