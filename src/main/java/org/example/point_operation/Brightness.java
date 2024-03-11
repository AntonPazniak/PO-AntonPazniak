package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.Slider;
import org.example.window.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Brightness {

    public static void convert(PortableAnymap portableAnymap, Window window) {
        Slider slider = new Slider("Brightness", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
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

    private static void increaseContrast(int[][][] imageMatrix, double factor) {
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
    }


}
