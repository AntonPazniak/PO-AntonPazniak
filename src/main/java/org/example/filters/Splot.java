package org.example.filters;

public class Splot {

    private static float[][] sobel = {
            {1, 0, -1},
            {2, 0, -2},
            {1, 0, -1}
    };

    private static float[][] roberts = {
            {1, 0},
            {0, -1}
    };

    private static float[][] laplace = {
            {0, 1, 0},
            {1, -4, 1},
            {0, 1, 0}
    };

    private static float[][] LoG = {
            {0, 0, -1, 0, 0},
            {0, -1, -2, -1, 0},
            {-1, -2, 16, -2, -1},
            {0, -1, -2, -1, 0},
            {0, 0, -1, 0, 0}
    };

    private static float[][] previt = {
            {1, 0, -1},
            {1, 0, -1},
            {1, 0, -1}
    };

    public static void sobel(int[][][] matrix) {
        test(sobel, matrix);
    }

    public static void roberts(int[][][] matrix) {
        test(roberts, matrix);
    }

    public static void laplace(int[][][] matrix) {
        test(laplace, matrix);
    }

    public static void loG(int[][][] matrix) {
        test(LoG, matrix);
    }

    public static void previt(int[][][] matrix) {
        test(previt, matrix);
    }

    public static void test(float[][] splotMatrix, int[][][] imageMatrix) {
        int[][][] outputMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];

        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                float red = 0;
                float green = 0;
                float blue = 0;
                int n = splotMatrix.length * splotMatrix[0].length;
                for (int z = 0; z < splotMatrix.length; z++) {
                    for (int r = 0; r < splotMatrix[z].length; r++) {
                        int indexRow = x - splotMatrix.length / 2 + z;
                        int indexCol = y - splotMatrix[z].length / 2 + r;
                        if (indexRow >= 0 && indexRow < imageMatrix.length && indexCol >= 0 && indexCol < imageMatrix[x].length) {
                            red += imageMatrix[indexRow][indexCol][0] * splotMatrix[z][r];
                            green += imageMatrix[indexRow][indexCol][1] * splotMatrix[z][r];
                            blue += imageMatrix[indexRow][indexCol][2] * splotMatrix[z][r];
                        } else {
                            n = n - 1;
                        }
                    }
                }

                red /= n;
                green /= n;
                blue /= n;

                outputMatrix[x][y][0] = (int) Math.min(255, Math.max(0, red));
                outputMatrix[x][y][1] = (int) Math.min(255, Math.max(0, green));
                outputMatrix[x][y][2] = (int) Math.min(255, Math.max(0, blue));
            }
        }

        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                for (int z = 0; z < 3; z++) {
                    imageMatrix[x][y][z] = outputMatrix[x][y][z];
                }
            }
        }
    }


}