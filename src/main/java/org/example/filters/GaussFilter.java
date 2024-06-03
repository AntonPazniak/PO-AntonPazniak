package org.example.filters;

import org.example.models.PortableAnymap;
import org.jetbrains.annotations.NotNull;

import static org.example.filters.Splot.applySplot;

public class GaussFilter {

    public static void convert(@NotNull PortableAnymap image, int size, double step) {
        int[][][] imageMatrix = image.getMatrix();
        image.setMatrix(applySplot(createGaussianKernel(size, step), imageMatrix));
    }

    @NotNull
    public static int[][][] convert(int[][][] imageMatrix, int size, double step) {
        return applySplot(createGaussianKernel(size, step), imageMatrix);
    }

    @NotNull
    public static double[][][] convert(double[][][] imageMatrix, int size, double step) {
        return applySplotFloat(createGaussianKernel(size, step), imageMatrix);
    }

    @NotNull
    private static float[][] createGaussianKernel(int size, double sigma) {
        float[][] kernel = new float[size][size];
        float sum = 0;

        int halfSize = size / 2;
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int y = -halfSize; y <= halfSize; y++) {
                float value = (float) (Math.exp(-(x * x + y * y) / (2 * sigma * sigma)) / (2 * Math.PI * sigma * sigma));
                kernel[x + halfSize][y + halfSize] = value;
                sum += value;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                kernel[i][j] /= sum;
            }
        }

        return kernel;
    }

    public static double[][][] applySplotFloat(float[][] splotMatrix, double[][][] imageMatrix) {
        int startPos = splotMatrix.length / 2;
        var newMatrix = new double[imageMatrix.length][imageMatrix[0].length][3];
        var expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < imageMatrix.length + startPos; x++) {
            for (int y = startPos; y < imageMatrix[0].length + startPos; y++) {
                var color = new double[3];
                for (int z = 0; z < 3; z++) {
                    int newPixel = (int) Math.min(255,
                            Math.max(0,
                                    getCore2(splotMatrix, expandMatrix, startPos, x, y, z)));
                    color[z] = newPixel;
                }
                newMatrix[x - startPos][y - startPos] = color;
            }
        }


        return newMatrix;
    }

    public static double[][][] expandMatrix(double[][][] matrix, int sizeIncrease) {
        int newWidth = matrix.length + 2 * sizeIncrease;
        int newHeight = matrix[0].length + 2 * sizeIncrease;
        var newMatrix = new double[newWidth][newHeight][3];

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                if (i < sizeIncrease || i >= matrix.length + sizeIncrease ||
                        j < sizeIncrease || j >= matrix[0].length + sizeIncrease) {
                    newMatrix[i][j] = new double[]{0, 0, 0};
                } else {
                    newMatrix[i][j] = matrix[i - sizeIncrease][j - sizeIncrease];
                }
            }
        }

        return newMatrix;
    }

    public static double getCore2(float[][] splotMatrix, double[][][] expandMatrix, int startPos, int x, int y, int z) {
        double pixel = 0;
        for (int i = 0; i < splotMatrix.length; i++) {
            for (int j = 0; j < splotMatrix[i].length; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][z];
            }
        }
        return Math.abs(pixel);
    }

}
