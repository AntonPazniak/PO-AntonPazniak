package org.example.window;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.point_operation.Brightness;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class BrightnessWindow extends SliderWindow {

    private BrightnessWindow(String title, int max, int min, int start) {
        super(title, max, min, start);
    }

    public static void createWindow(PortableAnymap image) {
        BrightnessWindow slider = new BrightnessWindow("Brightness", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = image.copyMatrix();
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                Brightness.increaseContrast(workMatrix, luminosity);
                image.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                MainWindow.getMainWindow().resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                Brightness.convert(image, luminosity);
                image.updateImage();
                MainWindow.getMainWindow().resizeImage();
                slider.dispose();
            }
        });
    }

}
