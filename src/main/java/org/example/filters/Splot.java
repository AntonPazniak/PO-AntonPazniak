package org.example.filters;

public class Splot {

    public static void test(float[][] splotMatrix, int[][][] imageMatrix) {
        int[][][] outputMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];

        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                float red = 0;
                float green = 0;
                float blue = 0;

                for (int z = 0; z < splotMatrix.length; z++) {
                    for (int r = 0; r < splotMatrix[z].length; r++) {
                        int indexRow = x - splotMatrix.length / 2 + z;
                        int indexCol = y - splotMatrix[z].length / 2 + r;
                        if (indexRow >= 0 && indexRow < imageMatrix.length && indexCol >= 0 && indexCol < imageMatrix[x].length) {
                            red += imageMatrix[indexRow][indexCol][0] * splotMatrix[z][r];
                            green += imageMatrix[indexRow][indexCol][1] * splotMatrix[z][r];
                            blue += imageMatrix[indexRow][indexCol][2] * splotMatrix[z][r];
                        }
                    }
                }

                red /= splotMatrix.length * splotMatrix[0].length;
                green /= splotMatrix.length * splotMatrix[0].length;
                blue /= splotMatrix.length * splotMatrix[0].length;

                outputMatrix[x][y][0] = (int) red;
                outputMatrix[x][y][1] = (int) green;
                outputMatrix[x][y][2] = (int) blue;
            }
        }

        // Копируем значения outputMatrix обратно в imageMatrix
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                for (int z = 0; z < 3; z++) {
                    imageMatrix[x][y][z] = outputMatrix[x][y][z];
                }
            }
        }
    }


}