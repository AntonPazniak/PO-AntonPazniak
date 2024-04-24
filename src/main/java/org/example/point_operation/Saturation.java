package org.example.point_operation;

import org.example.models.PortableAnymap;

public class Saturation {

    public static void convert(PortableAnymap portableAnymap, double factor) {


    }


    public static void increaseSaturation(int[][][] image, double saturationFactor) {
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
