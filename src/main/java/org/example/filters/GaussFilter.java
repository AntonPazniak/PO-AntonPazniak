package org.example.filters;

import org.example.models.PortableAnymap;

import static org.example.filters.Splot.applySplot;

public class GaussFilter {

    public static void convert(PortableAnymap image, int size, double step) {
        int[][][] imageMatrix = image.getMatrix();
        image.setMatrix(applySplot(createGaussianKernel(size, step), imageMatrix));
    }

    public static int[][][] convert(int[][][] imageMatrix, int size, double step) {
        return applySplot(createGaussianKernel(size, step), imageMatrix);
    }

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

}
