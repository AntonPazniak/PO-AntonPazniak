package org.example.canny;

import org.example.filters.GaussFilter;
import org.example.filters.SobelFilter;
import org.example.models.Direction;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public final class CannyEdgeDetector {

    private static final int[] witheColor = new int[]{255, 255, 255};
    private static final int[] blackColor = new int[]{0, 0, 0};

    public static void convert(PortableAnymap image) {
        Desaturation.convert(image);
        var imageMatrix = image.getMatrix();
        GaussFilter.convert(imageMatrix, 5, 1);
        var sobel = new SobelFilter();
        imageMatrix = sobel.applySobel(image.getMatrix());
        var dir = sobel.getAngelMatrix();
        image.setMatrix(
                applyDoubleThreshold(
                        applyNonMaxSuppression(imageMatrix, dir), 10, 100));
        image.updateImage();
    }

    public static int[][][] applyNonMaxSuppression(int[][][] gradientMagnitude, int[][] dirMatrix) {
        int height = gradientMagnitude.length;
        int width = gradientMagnitude[0].length;
        int[][][] newImageMatrix = new int[height][width][3];

        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                var color = gradientMagnitude[i][j];
                int dir = dirMatrix[i][j];

                if (dir != -1) {
                    var direction = new Direction(dir);

                    int currentGradientMag = gradientMagnitude[i][j][0];
                    int neighborGradientMag1 = gradientMagnitude[i + direction.y][j + direction.x][0];
                    int neighborGradientMag2 = gradientMagnitude[i - direction.y][j - direction.x][0];

                    if (currentGradientMag >= neighborGradientMag1 && currentGradientMag > neighborGradientMag2) {
                        newImageMatrix[i][j] = color;
                    } else {
                        newImageMatrix[i][j] = blackColor;
                    }
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

                if (pixel < lowThreshold) {
                    newImageMatrix[i][j] = blackColor;
                } else if (pixel > highThreshold) {
                    newImageMatrix[i][j] = witheColor;
                }
            }
        }

        for (int i = 0; i < gradientMagnitude.length; i++) {
            for (int j = 0; j < gradientMagnitude[0].length; j++) {
                if (gradientMagnitude[i][j][0] > lowThreshold && gradientMagnitude[i][j][0] < highThreshold) {
                    boolean hasWhiteNeighbor = false;

                    // Check top neighbor
                    if (i > 0 && gradientMagnitude[i - 1][j][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (i < gradientMagnitude.length - 1 && gradientMagnitude[i + 1][j][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (j > 0 && gradientMagnitude[i][j - 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (j < gradientMagnitude[0].length - 1 && gradientMagnitude[i][j + 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (i > 0 && j > 0 && gradientMagnitude[i - 1][j - 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (i < gradientMagnitude.length - 1 && j < gradientMagnitude[0].length - 1 && gradientMagnitude[i + 1][j + 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (i < gradientMagnitude.length - 1 && j > 0 && gradientMagnitude[i + 1][j - 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }
                    if (i > 0 && j < gradientMagnitude[0].length - 1 && gradientMagnitude[i - 1][j + 1][0] == 255) {
                        hasWhiteNeighbor = true;
                    }

                    if (hasWhiteNeighbor) {
                        newImageMatrix[i][j] = witheColor;
                    } else {
                        newImageMatrix[i][j] = blackColor;
                    }
                }
            }
        }

        return newImageMatrix;
    }


}
