package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.Slider;
import org.example.window.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Brightness {
    private static int[][][] newMatrix;

    public static void convert(PortableAnymap portableAnymap, Window window) {
        Slider slider = new Slider("Brightness", 100, -100, 0);
        newMatrix = copyMatrix(portableAnymap.getMatrix());
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = copyMatrix(newMatrix);
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseContrast(workMatrix, luminosity);
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                window.resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseContrast(portableAnymap.getMatrix(), luminosity);
                portableAnymap.updateImage();
                window.resizeImage();
                slider.getFrame().dispose();
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

    public static int[][][] copyMatrix(int[][][] original) {
        int[][][] copy = new int[original.length][][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new int[original[i].length][];
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = new int[original[i][j].length];
                System.arraycopy(original[i][j], 0, copy[i][j], 0, original[i][j].length);
            }
        }
        return copy;
    }


}
