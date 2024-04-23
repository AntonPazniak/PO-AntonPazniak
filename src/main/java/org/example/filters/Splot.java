package org.example.filters;

import org.example.models.PortableAnymap;

public class Splot {

    private static final float[][] sobel = {
            {1, 0, -1},
            {2, 0, -2},
            {1, 0, -1}
    };

    private static final float[][] roberts = {
            {1, 0},
            {0, -1}
    };

    private static final float[][] laplace = {
            {0, 1, 0},
            {1, -4, 1},
            {0, 1, 0}
    };

    private static final float[][] LoG = {
            {0, 0, -1, 0, 0},
            {0, -1, -2, -1, 0},
            {-1, -2, 16, -2, -1},
            {0, -1, -2, -1, 0},
            {0, 0, -1, 0, 0}
    };

    private static final float[][] previt = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
    };

    public static void sobel(int[][][] matrix) {
        starSplot(sobel, matrix);
    }

    public static void roberts(int[][][] matrix) {
        starSplot(roberts, matrix);
    }

    public static void laplace(int[][][] matrix) {
        starSplot(laplace, matrix);
    }

    public static void loG(int[][][] matrix) {
        starSplot(LoG, matrix);
    }

    public static void previt(int[][][] matrix) {
        starSplot(previt, matrix);
    }

    public static void gauss(PortableAnymap image, int size, int step) {
        int[][][] imageMatrix = image.getMatrix();
        image.setMatrix(test(createGaussianKernel(size, step), imageMatrix));
    }


    public static float[][] createGaussianKernel(int size, float sigma) {
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

    public static void starSplot(float[][] splotMatrix, int[][][] imageMatrix) {
        sumMatrix(test(splotMatrix, imageMatrix), test(transpose(splotMatrix), imageMatrix), imageMatrix);
    }

    public static int[][][] test(float[][] splotMatrix, int[][][] imageMatrix) {
        int kernelSize = splotMatrix.length;
        int startPos = kernelSize / 2;
        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);

        for (int x = startPos; x < expandMatrix.length - startPos; x++) {
            for (int y = startPos; y < expandMatrix[0].length - startPos; y++) {
                float red = 0;
                float green = 0;
                float blue = 0;

                for (int i = 0; i < kernelSize; i++) {
                    for (int j = 0; j < kernelSize; j++) {
                        red += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][0];
                        green += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][1];
                        blue += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][2];
                    }
                }

                int[] color = new int[3];
                color[0] = (int) Math.min(255, Math.max(0, red));
                color[1] = (int) Math.min(255, Math.max(0, green));
                color[2] = (int) Math.min(255, Math.max(0, blue));

                newMatrix[x - startPos][y - startPos] = color;
            }
        }

        return newMatrix;
    }

    private static int[][][] expandMatrix(int[][][] matrix, int sizeIncrease) {
        int[][][] newMatrix = new int[matrix.length + sizeIncrease][matrix[0].length + sizeIncrease][3];
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix[0].length; j++) {
                if (i < sizeIncrease || i > newMatrix.length - 1 - sizeIncrease) {
                    newMatrix[i][j] = new int[]{1, 1, 1};
                } else if (j < sizeIncrease || j > newMatrix[0].length - 1 - sizeIncrease) {
                    newMatrix[i][j] = new int[]{1, 1, 1};
                } else
                    newMatrix[i][j] = matrix[i][j];
            }
        }
        return newMatrix;
    }


    public static float[][] transpose(float[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        float[][] transposedMatrix = new float[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }

        return transposedMatrix;
    }

    private static void sumMatrix(int[][][] xMatrix, int[][][] yMatrix, int[][][] imageMatrix) {
        for (int i = 0; i < xMatrix.length; i++) {
            for (int j = 0; j < xMatrix[0].length; j++) {
                imageMatrix[i][j] = new int[]{
                        Math.min(255, Math.max(0, xMatrix[i][j][0] + yMatrix[i][j][0])),
                        Math.min(255, Math.max(0, xMatrix[i][j][0] + yMatrix[i][j][1])),
                        Math.min(255, Math.max(0, xMatrix[i][j][0] + yMatrix[i][j][2]))
                };
            }
        }
    }

}