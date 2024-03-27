package org.example.filters;

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
        int startPos = splotMatrix.length / 2;
        int sizeSplot = splotMatrix.length * splotMatrix[0].length;
        int[][][] expandMatrix = expandMatrix(imageMatrix, startPos);
        for (int x = startPos; x < expandMatrix.length - 1 - startPos; x++) {
            for (int y = startPos; y < expandMatrix[0].length - 1 - startPos; y++) {
                float read = 0;
                float green = 0;
                float blue = 0;
                for (int i = 0; i < splotMatrix.length; i++) {
                    for (int j = 0; j < splotMatrix[0].length; j++) {
                        read += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][0];
                        green += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][1];
                        blue += splotMatrix[i][j] * expandMatrix[x - startPos + i][y - startPos + j][2];
                    }
                }
                int[] color = new int[3];
                color[0] = (int) Math.min(255, Math.max(0, read / 2));
                color[1] = (int) Math.min(255, Math.max(0, green / 2));
                color[2] = (int) Math.min(255, Math.max(0, blue / 2));
                imageMatrix[x - startPos][y - startPos] = color;
            }
        }

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

    private static void print(int[][][] matrix) {
        for (int[][] i : matrix) {
            for (int[] j : i) {
                System.out.print("(");
                for (int k : j) {
                    System.out.print(k + " ");
                }
                System.out.print(")");
            }
            System.out.println();
        }
    }

}