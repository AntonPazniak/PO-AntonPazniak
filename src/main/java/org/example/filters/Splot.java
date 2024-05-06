package org.example.filters;

import lombok.Getter;
import org.example.models.PortableAnymap;

public class Splot {

    private static final float[][] sobelX = {
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

    public static int[][][] applySplot(float[][] splotMatrix, int[][][] imageMatrix) {
        int startPos = splotMatrix.length / 2;
        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < imageMatrix.length; x++) {
            for (int y = startPos; y < imageMatrix[x].length + 1; y++) {
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

    @Getter
    private static int[][] angelMatrix;

    public static int[][][] applySobel(int[][][] imageMatrix) {
        int startPos = 1;
        float[][] sobelY = transpose(sobelX);

        angelMatrix = new int[imageMatrix.length][imageMatrix[0].length];

        int[][][] newMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);

        for (int x = startPos; x < imageMatrix.length; x++) {
            for (int y = startPos; y < imageMatrix[0].length; y++) {

                int[] color = new int[3];
                for (int z = 0; z < 3; z++) {
                    var pixelX = getCore(sobelX, expandMatrix, startPos, x, y, z);
                    var pixelY = getCore(sobelY, expandMatrix, startPos, x, y, z);

                    color[z] = (int) Math.min(255,
                            Math.max(0,
                                    Math.sqrt((
                                            Math.pow(pixelX, 2) +
                                                    Math.pow(pixelY, 2)))));

                    var angel = calculateAngle(pixelX, pixelY);
                    angelMatrix[x][y] = getAngelMatrix(angel);
                    //System.out.println(angelMatrix[x][y]);
                }

                newMatrix[x - startPos][y - startPos] = color;
            }
        }

        return newMatrix;
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
//        double angleRadians = Math.atan2(y, x); // Calculate angle in radians
//        return Math.toDegrees(angleRadians);
        var angle = Math.pow(
                Math.tan(y / x), -1
        );
        return Math.toDegrees(angle);
    }

    private static float getCore(float[][] splotMatrix, int[][][] expandMatrix, int startPos, int x, int y, int z) {
        float pixel = 0;
        for (int i = 0; i < splotMatrix.length; i++) {
            for (int j = 0; j < splotMatrix[i].length; j++) {
                pixel += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][z];
            }
        }
        return Math.abs(pixel
//                /splotMatrix.length
        );
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