package org.example.point_operation;

import org.example.models.PortableAnymap;
import org.example.pars.Convert;

import javax.swing.*;
import java.io.File;

public class SDP {


    public static void convert(int mun, PortableAnymap portableAnymap) {

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        PortableAnymap portableAnymap1;

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            int dotIndex = file.getName().lastIndexOf('.');
            if (dotIndex > 0) {
                s String fileExtension = file.getName().substring(dotIndex + 1);
                portableAnymap1 = Convert.open(file.getAbsolutePath());
                switch (mun) {
                    case 0 -> sum(portableAnymap, portableAnymap1);
                    case 1 -> dif(portableAnymap, portableAnymap1);
                    case 2 -> product(portableAnymap, portableAnymap1);
                }
            }
        }
        portableAnymap.updateImage();
    }


    private static void sum(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        matrix[i][j][l] = Math.min(255,
                                Math.max(0, matrix[i][j][l] + portableAnymap1.getMatrix()[i][j][l]));
                    }
                }
            }
        }
    }


    private static void dif(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        matrix[i][j][l] = Math.min(255,
                                Math.max(0, matrix[i][j][l] - portableAnymap1.getMatrix()[i][j][l]));
                    }
                }
            }
        }
    }

    private static void product(PortableAnymap portableAnymap, PortableAnymap portableAnymap1) {
        int[][][] matrix = portableAnymap.getMatrix();
        for (int i = 0; i < portableAnymap.getHeight(); i++) {
            for (int j = 0; j < portableAnymap.getWidth(); j++) {
                if (i < portableAnymap1.getHeight() && j < portableAnymap1.getWidth()) {
                    for (int l = 0; l < 3; l++) {
                        int pixel = portableAnymap.getMatrix()[i][j][l] * (portableAnymap1.getMatrix()[i][j][l] / 255);
                        matrix[i][j][l] = pixel;
                    }
                }
            }
        }
    }
}
