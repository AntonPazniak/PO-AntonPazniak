package org.example.window;

import org.example.models.PortableAnymap;
import org.example.pars.CreateImageFromMatrix;
import org.example.point_operation.Contrast;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ContrastWindow extends SliderWindow {

    private ContrastWindow(String title, int max, int min, int start) {
        super(title, max, min, start);
    }

    public static void CreateWindow(PortableAnymap portableAnymap) {
        ContrastWindow slider = new ContrastWindow("Test", 100, -100, 0);
        slider.getApplyButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][][] workMatrix = portableAnymap.copyMatrix();
                Contrast.increaseContrast(workMatrix,
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.setImage(CreateImageFromMatrix.createImageFromRGBMatrix(workMatrix));
                MainWindow.getMainWindow().resizeImage();
            }
        });
        slider.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Contrast.increaseContrast(portableAnymap.getMatrix(),
                        getFactor(slider.getSlider().getValue()));
                portableAnymap.updateImage();
                MainWindow.getMainWindow().resizeImage();
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


}
