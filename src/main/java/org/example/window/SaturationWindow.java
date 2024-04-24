package org.example.window;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.example.point_operation.Saturation.increaseSaturation;

public final class SaturationWindow extends SliderWindow {


    private SaturationWindow(String title, int max, int min, int start) {
        super(title, max, min, start);
    }


    public static void createWindow(PortableAnymap image) {
        SaturationWindow slider = new SaturationWindow("Saturation", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = image.copyMatrix();
                increaseSaturation(workMatrix,
                        getFactor(slider.getSlider().getValue()));
                image.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                MainWindow.getMainWindow().resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                float luminosity = ((float) slider.getSlider().getValue() / 100);
                increaseSaturation(image.getMatrix(),
                        getFactor(slider.getSlider().getValue()));
                image.updateImage();
                MainWindow.getMainWindow().resizeImage();
                slider.dispose();
            }
        });
    }


    public static float getFactor(float factor) {
        if (factor > 0) {
            factor = factor / 10;
        } else {
            factor = Math.abs(factor / 100 + 1);
        }
        return factor;
    }

}
