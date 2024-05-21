package org.example.filters;

import lombok.Getter;
import org.example.models.PortableAnymap;
import org.jetbrains.annotations.NotNull;

import static org.example.filters.Splot.expandMatrix;

@Getter
public class SobelFilter {

    private final int[][] sobelMatrixX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private final int[][] sobelMatrixY = {
            {-1, -2, -1},
            {0, 0, 0},
            {1, 2, 1}
    };
    private int[][] angelMatrix;

    private double[][][] imageMatrixSobelX;
    private double[][][] imageMatrixSobelY;

    public SobelFilter() {
    }

    public SobelFilter(@NotNull PortableAnymap image) {
        var sobel = new SobelFilter();
        image.setMatrix(sobel.applySobel(image.getMatrix()));
    }

    public static void convert(PortableAnymap image) {
        var sobel = new SobelFilter(image);
    }

    @NotNull
    static double[][][] applySplot(@NotNull int[][] splotMatrix, @NotNull int[][][] imageMatrix) {
        int startPos = splotMatrix.length / 2;
        var newMatrix = new double[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < imageMatrix.length + startPos; x++) {
            for (int y = startPos; y < imageMatrix[0].length + startPos; y++) {
                double[] color = new double[3];
                for (int z = 0; z < 3; z++) {
                    var newPixel = getCore(splotMatrix, expandMatrix, startPos, x, y, z);
                    color[z] = newPixel;
                }
                newMatrix[x - startPos][y - startPos] = color;
            }
        }


        return newMatrix;
    }

    private static int getAngel(double angle) {

        if (angle >= -22.5 && angle <= 22.5) {
            return 0;
        } else if (angle >= 22.5 && angle <= 67.5) {
            return 45;
        } else if (angle >= 67.5 && angle <= 112.5) {
            return 90;
        } else if (angle >= 112.5 && angle <= 157.5) {
            return 135;
        } else if (angle >= 157.5 && angle <= 202.5) {
            return 180;
        } else if (angle <= -22.5 && angle >= -67.5) {
            return -45;
        } else if (angle <= -67.5 && angle >= -112.5) {
            return -90;
        } else if (angle <= -112.5 && angle >= -157.5) {
            return -135;
        } else {
            return -1;
        }
    }

    private static double calculateAngle(double x, double y) {
        var angle = Math.atan2(y, x);
        return Math.toDegrees(angle);
    }

    private static float getCore(int[][] splotMatrix, int[][][] expandMatrix, int startPos, int x, int y, int z) {
        float pixel = 0;
        for (int i = 0; i < splotMatrix.length; i++) {
            for (int j = 0; j < splotMatrix[i].length; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][z];
            }
        }
        return pixel;
    }

    @NotNull
    public int[][][] applySobel(@NotNull int[][][] imageMatrix) {
        angelMatrix = new int[imageMatrix.length][imageMatrix[0].length];
        imageMatrixSobelX = applySplot(sobelMatrixX, imageMatrix);
        imageMatrixSobelY = applySplot(sobelMatrixY, imageMatrix);
        var newImageMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[0].length; y++) {
                var color = new int[3];
                for (int z = 0; z < 3; z++) {
                    var pixelX = imageMatrixSobelX[x][y][z];
                    var pixelY = imageMatrixSobelY[x][y][z];
                    color[z] = (int) Math.min(255,
                            Math.max(0,
                                    Math.sqrt((pixelX * pixelX + pixelY * pixelY))));
                    angelMatrix[x][y] = (pixelX == 0 && pixelY == 0) ?
                            (-1) : (getAngel(calculateAngle(pixelX, pixelY)));

                }
                newImageMatrix[x][y] = color;
            }

        }
        return newImageMatrix;
    }

}
