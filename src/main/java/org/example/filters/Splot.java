package org.example.filters;

import lombok.Getter;
import org.example.models.PortableAnymap;
import org.jetbrains.annotations.NotNull;

public class Splot {

    private static final float[][] s = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
    };

    private static final float[][] sobel = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
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
        image.setMatrix(applySobel(image.getMatrix()));
    }

    public static void roberts(PortableAnymap image) {
        starSplot(ROBERTS, image.getMatrix());
        image.updateImage();
    }

    public static void laplace(PortableAnymap image) {
        starSplot(LAPLACE, image.getMatrix());
        image.updateImage();
    }

    public static void loG(PortableAnymap image) {
        starSplot(LOG, image.getMatrix());
        image.updateImage();
    }

    public static void previt(PortableAnymap image) {
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
    @Getter
    private static int[][][] angelMatrix;

    public static int[][][] applySplot(float[][] splotMatrix, int[][][] imageMatrix) {
        int startPos = splotMatrix.length / 2;
        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < imageMatrix.length + startPos; x++) {
            for (int y = startPos; y < imageMatrix[0].length + startPos; y++) {
                int[] color = new int[3];
                for (int z = 0; z < 3; z++) {
                    int newPixel = (int) Math.min(255,
                            Math.max(0,
                                    getCore(splotMatrix, expandMatrix, startPos, x, y, z)));
                    color[z] = newPixel;
                }
                newMatrix[x - startPos][y - startPos] = color;
            }
        }


        return newMatrix;
    }

    @NotNull
    public static double[][][] applySplot2(@NotNull float[][] splotMatrix, @NotNull int[][][] imageMatrix) {
        int startPos = splotMatrix.length / 2;
        var newMatrix = new double[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < imageMatrix.length + startPos; x++) {
            for (int y = startPos; y < imageMatrix[0].length + startPos; y++) {
                double[] color = new double[3];
                for (int z = 0; z < 3; z++) {
                    var newPixel = getCore2(splotMatrix, expandMatrix, startPos, x, y, z);
                    color[z] = newPixel;
                }
                newMatrix[x - startPos][y - startPos] = color;
            }
        }


        return newMatrix;
    }

    @NotNull
    public static int[][][] applySobel(@NotNull int[][][] imageMatrix) {
        angelMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        var sobelX = applySplot2(sobel, imageMatrix);
        var sobelY = applySplot2(transpose(sobel), imageMatrix);
        var newImageMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[0].length; y++) {
                var color = new int[3];
                for (int z = 0; z < 3; z++) {
                    var pixelX = sobelX[x][y][z];
                    var pixelY = sobelY[x][y][z];
                    color[z] = (int) Math.min(255,
                            Math.max(0,
                                    Math.sqrt((pixelX * pixelX + pixelY * pixelY))));
                    var angel = calculateAngle(pixelX, pixelY);
                    angelMatrix[x][y][z] = getAngelMatrix(angel);
                }
                newImageMatrix[x][y] = color;
            }
        }
        return newImageMatrix;
    }

    private static int getAngelMatrix(double angle) {
        if (angle < 0) {
            angle += 360.;
        }
        if (angle <= 22.5 || (angle >= 157.5 && angle <= 202.5) || angle >= 337.5) {
            return 0;
        } else if ((angle >= 22.5 && angle <= 67.5) || (angle >= 202.5 && angle <= 247.5)) {
            return 45;
        } else if ((angle >= 67.5 && angle <= 112.5) || (angle >= 247.5 && angle <= 292.5)) {
            return 90;
        } else {
            return 135;
        }
    }

    public static double calculateAngle(double x, double y) {
        var angle = Math.atan(y / x);
        return Math.toDegrees(angle);
    }

    private static float getCore(float[][] splotMatrix, int[][][] expandMatrix, int startPos, int x, int y, int z) {
        float pixel = 0;
        for (int i = 0; i < splotMatrix.length; i++) {
            for (int j = 0; j < splotMatrix[i].length; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][z];
            }
        }
        return Math.abs(pixel);
    }

    private static float getCore2(float[][] splotMatrix, int[][][] expandMatrix, int startPos, int x, int y, int z) {
        float pixel = 0;
        for (int i = 0; i < splotMatrix.length; i++) {
            for (int j = 0; j < splotMatrix[i].length; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][z];
            }
        }
        return pixel;
    }


    private static int[][][] expandMatrix(int[][][] matrix, int sizeIncrease) {
        int newWidth = matrix.length + 2 * sizeIncrease;
        int newHeight = matrix[0].length + 2 * sizeIncrease;
        int[][][] newMatrix = new int[newWidth][newHeight][3];

        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                if (i < sizeIncrease || i >= matrix.length + sizeIncrease ||
                        j < sizeIncrease || j >= matrix[0].length + sizeIncrease) {
                    newMatrix[i][j] = new int[]{0, 0, 0};
                } else {
                    newMatrix[i][j] = matrix[i - sizeIncrease][j - sizeIncrease];
                }
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