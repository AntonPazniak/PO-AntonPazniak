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
//        image.setMatrix(applyNonMaxSuppression(image.getMatrix(), dir));
        image.setMatrix(
                applyDoubleThreshold(
                        applyNonMaxSuppression(image.getMatrix(), dir), 100, 200));
        image.updateImage();
    }

    public static int[][][] applyNonMaxSuppression(int[][][] gradientMagnitude, int[][] dirMatrix) {
        int height = gradientMagnitude.length;
        int width = gradientMagnitude[0].length;
        int[][][] newImageMatrix = new int[gradientMagnitude.length][gradientMagnitude[0].length][3];


        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                int color = gradientMagnitude[i][j][0];
                int dir = dirMatrix[i][j];

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
        int[][][] newImagMatrix = new int[gradientMagnitude.length][gradientMagnitude[0].length][3];

        for (int i = 0; i < gradientMagnitude.length; i++) {
            for (int j = 0; j < gradientMagnitude[0].length; j++) {
                int pixel = gradientMagnitude[i][j][0];
                if (pixel < lowThreshold) {
                    newImagMatrix[i][j] = blackColor;
                } else if (pixel > highThreshold) {
                    newImagMatrix[i][j] = witheColor;
                } else {
                    boolean hasStrongNeighbor = false;
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            if (i + di >= 0 && i + di < gradientMagnitude.length &&
                                    j + dj >= 0 && j + dj < gradientMagnitude[0].length &&
                                    gradientMagnitude[i + di][j + dj][0] > highThreshold) {
                                hasStrongNeighbor = true;
                                break;
                            }
                        }
                        if (hasStrongNeighbor) {
                            break;
                        }
                    }
                    if (hasStrongNeighbor) {
                        newImagMatrix[i][j] = witheColor;
                    } else {
                        newImagMatrix[i][j] = blackColor;
                    }
                }
            }
        }
        return newImagMatrix;
    }


}
