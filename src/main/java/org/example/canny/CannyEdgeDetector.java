package org.example.canny;

import org.example.filters.Splot;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public final class CannyEdgeDetector {

    private static final int[] witheColor = new int[]{255, 255, 255};
    private static final int[] blackColor = new int[]{0, 0, 0};

    public static void convert(PortableAnymap image) {
        Desaturation.convert(image);
        Splot.gauss(image, 5, 1);
        Splot.sobel(image);
        var dir = Splot.getAngelMatrix();

        //      image.setMatrix(applyNonMaxSuppression(image.getMatrix(), dir));
        image.setMatrix(
                applyDoubleThreshold(
                        applyNonMaxSuppression(image.getMatrix(), dir), 40, 200));
        image.updateImage();
    }

    public static int[][][] applyNonMaxSuppression(int[][][] gradientMagnitude, int[][][] dirMatrix) {
        int height = gradientMagnitude.length;
        int width = gradientMagnitude[0].length;
        int[][][] newImageMatrix = new int[height][width][3];
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int color = gradientMagnitude[i][j][0];
                int dir = dirMatrix[i][j][0];

                int neighbor1 = 255, neighbor2 = 255;
                if (dir == 0) {
                    neighbor1 = gradientMagnitude[i][j + 1][0];
                    neighbor2 = gradientMagnitude[i][j - 1][0];
                } else if (dir == 45) {
                    neighbor1 = gradientMagnitude[i + 1][j - 1][0];
                    neighbor2 = gradientMagnitude[i - 1][j + 1][0];
                } else if (dir == 90) {
                    neighbor1 = gradientMagnitude[i + 1][j][0];
                    neighbor2 = gradientMagnitude[i - 1][j][0];
                } else if (dir == 135) {
                    neighbor1 = gradientMagnitude[i - 1][j - 1][0];
                    neighbor2 = gradientMagnitude[i + 1][j + 1][0];
                }

                if (color >= neighbor1 && color >= neighbor2) {
                    newImageMatrix[i][j] = gradientMagnitude[i][j];
                } else {
                    gradientMagnitude[i][j] = blackColor;
                }
            }
        }
        return newImageMatrix;
    }


    public static int[][][] applyDoubleThreshold(int[][][] gradientMagnitude, int lowThreshold, int highThreshold) {
        int[][][] newImageMatrix = new int[gradientMagnitude.length][gradientMagnitude[0].length][3];

        // Проходим по каждому пикселю входной матрицы
        for (int i = 0; i < gradientMagnitude.length; i++) {
            for (int j = 0; j < gradientMagnitude[0].length; j++) {
                int pixel = gradientMagnitude[i][j][0];

                // Проверяем значение пикселя относительно порогов
                if (pixel < lowThreshold) {
                    // Пиксель ниже нижнего порога, устанавливаем черный цвет
                    newImageMatrix[i][j] = blackColor;
                } else if (pixel > highThreshold) {
                    // Пиксель выше верхнего порога, устанавливаем белый цвет
                    newImageMatrix[i][j] = witheColor;
                } else {
                    // Пиксель между порогами, проверяем соседние пиксели на наличие белого цвета (255)
                    boolean hasWhiteNeighbor = false;

                    // Проверяем левого соседа (если существует и значение 255)
                    if (i > 0 && gradientMagnitude[i - 1][j][0] == 255) {
                        hasWhiteNeighbor = true;
                    }

                    // Проверяем правого соседа (если существует и значение 255)
                    if (i < gradientMagnitude.length - 1 && gradientMagnitude[i + 1][j][0] == 255) {
                        hasWhiteNeighbor = true;
                    }

                    // Проверяем верхнего соседа (если существует и значение 255)
                    if (j > 0 && gradientMagnitude[i][j - 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }

                    // Проверяем нижнего соседа (если существует и значение 255)
                    if (j < gradientMagnitude[0].length - 1 && gradientMagnitude[i][j + 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }

                    // Если есть хотя бы один белый сосед, устанавливаем пиксель в белый цвет
                    if (hasWhiteNeighbor) {
                        newImageMatrix[i][j] = witheColor;
                    } else {
                        // В противном случае, устанавливаем в черный цвет
                        newImageMatrix[i][j] = blackColor;
                    }
                }
            }
        }

        return newImageMatrix;
    }


}
