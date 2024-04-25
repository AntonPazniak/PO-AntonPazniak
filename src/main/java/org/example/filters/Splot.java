package org.example.filters;

import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public class Splot {

    private static final float[][] sobelX = {
            {1, 0, -1},
            {2, 0, -2},
            {1, 0, -1}
    };

    private static final float[][] ROBERTS = {
            {1, 0},
            {0, -1}
    };

    private static final float[][] LAPLACE = {
            {0, 1, 0},
            {1, -4, 1},
            {0, 1, 0}
    };

    private static final float[][] LOG = {
            {0, 0, -1, 0, 0},
            {0, -1, -2, -1, 0},
            {-1, -2, 16, -2, -1},
            {0, -1, -2, -1, 0},
            {0, 0, -1, 0, 0}
    };

    private static final float[][] PRIVET = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
    };

    public static void sobel(PortableAnymap image) {
        Desaturation.convert(image);
        image.setMatrix(applySobel(image.getMatrix()));
    }

    public static void roberts(PortableAnymap image) {
        Desaturation.convert(image);
        starSplot(ROBERTS, image.getMatrix());
        image.updateImage();
    }

    public static void laplace(PortableAnymap image) {
        Desaturation.convert(image);
        starSplot(LAPLACE, image.getMatrix());
        image.updateImage();
    }

    public static void loG(PortableAnymap image) {
        Desaturation.convert(image);
        starSplot(LOG, image.getMatrix());
        image.updateImage();
    }

    public static void previt(PortableAnymap image) {
        Desaturation.convert(image);
        starSplot(PRIVET, image.getMatrix());
        image.updateImage();
    }

    public static void gauss(PortableAnymap image, int size, double step) {
        int[][][] imageMatrix = image.getMatrix();
        image.setMatrix(applySplot(createGaussianKernel(size, step), imageMatrix));
    }


    public static float[][] createGaussianKernel(int size, double sigma) {
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
        sumMatrix(applySplot(splotMatrix, imageMatrix), applySplot(transpose(splotMatrix), imageMatrix), imageMatrix);
    }

    public static int[][][] applySplot(float[][] splotMatrix, int[][][] imageMatrix) {
        int kernelSize = splotMatrix.length;
        int startPos = kernelSize / 2;
        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);

        for (int x = startPos; x < expandMatrix.length - startPos; x++) {
            for (int y = startPos; y < expandMatrix[0].length - startPos; y++) {

                int newPixel = (int) Math.min(255,
                        Math.max(0,
                                getCore(splotMatrix, expandMatrix, kernelSize, startPos, x, y)));

                newMatrix[x][y] = new int[]{newPixel, newPixel, newPixel};
            }
        }

        return newMatrix;
    }

    public static int[][][] applySobel(int[][][] imageMatrix) {
        int kernelSize = 3;
        int startPos = 1;
        float[][] sobelY = transpose(sobelX);

        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);

        for (int x = startPos; x < imageMatrix.length; x++) {
            for (int y = startPos; y < imageMatrix[0].length; y++) {

                float pixelX = getCore(sobelX, expandMatrix, kernelSize, startPos, x, y);
                float pixelY = getCore(sobelY, expandMatrix, kernelSize, startPos, x, y);

                int newPixel = (int) Math.min(255,
                        Math.max(0,
                                Math.sqrt((
                                        Math.pow(pixelX, 2) +
                                                Math.pow(pixelY, 2)))));

                newMatrix[x][y] = new int[]{newPixel, newPixel, newPixel};
            }
        }

        return newMatrix;
    }

    private static float getCore(float[][] splotMatrix, int[][][] expandMatrix, int kernelSize, int startPos, int x, int y) {
        float pixel = 0;
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][0];
            }
        }
        return Math.abs(pixel);
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