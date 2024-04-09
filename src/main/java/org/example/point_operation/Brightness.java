package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.MainWindow;
import org.example.window.SliderWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Brightness implements PointFilter {

    @Override
    public void convert(PortableAnymap portableAnymap) {
        SliderWindow slider = new SliderWindow("Brightness", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseContrast(workMatrix, luminosity);
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                MainWindow.getMainWindow().resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseContrast(portableAnymap.getMatrix(), luminosity);
                portableAnymap.updateImage();
                MainWindow.getMainWindow().resizeImage();
                slider.dispose();
            }
        });

    }

    private void increaseContrast(int[][][] imageMatrix, double factor) {
        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                for (int k = 0; k < imageMatrix[i][j].length; k++) {
                    int color = imageMatrix[i][j][k];
                    color = Math.min(Math.max((int) (color + color * factor), 0), 255);
                    imageMatrix[i][j][k] = color;
                }
            }
        }
    }
}
