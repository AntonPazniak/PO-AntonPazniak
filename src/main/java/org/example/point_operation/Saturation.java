package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.window.MainWindow;
import org.example.window.SliderWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Saturation implements PointFilter {

    @Override
    public void convert(PortableAnymap portableAnymap) {
        SliderWindow slider = new SliderWindow("Saturation", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
                increaseSaturation(workMatrix,
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                MainWindow.getMainWindow().resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseSaturation(portableAnymap.getMatrix(),
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.updateImage();
                MainWindow.getMainWindow().resizeImage();
                slider.dispose();
            }
        });

    }

    private float getFactor(float factor) {
        if (factor > 0) {
            factor = factor / 10;
        } else {
            factor = Math.abs(factor / 100 + 1);
        }
        return factor;
    }

    private void increaseSaturation(int[][][] image, double saturationFactor) {
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                int red = image[i][j][0];
                int green = image[i][j][1];
                int blue = image[i][j][2];

                // Вычисление средней интенсивности цветов
                int intensity = (red + green + blue) / 3;

                // Изменение интенсивности каждого цвета
                int newRed = (int) (intensity + saturationFactor * (red - intensity));
                int newGreen = (int) (intensity + saturationFactor * (green - intensity));
                int newBlue = (int) (intensity + saturationFactor * (blue - intensity));

                // Ограничение значений в пределах [0, 255]
                newRed = Math.min(Math.max(newRed, 0), 255);
                newGreen = Math.min(Math.max(newGreen, 0), 255);
                newBlue = Math.min(Math.max(newBlue, 0), 255);

                // Присваивание новых значений
                image[i][j][0] = newRed;
                image[i][j][1] = newGreen;
                image[i][j][2] = newBlue;
            }
        }
    }

}
