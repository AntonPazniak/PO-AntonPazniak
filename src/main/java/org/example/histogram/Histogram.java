package org.example.histogram;

import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public class Histogram {


    public static double[][] getRGBHistogram(PortableAnymap image) {
        return getHistogramMatrix(image.getMatrix());
    }

    public static double[][] getGrayHistogram(PortableAnymap image) {
        int[][][] imageMatrix = Desaturation.convertP3(image.copyMatrix());
        return getHistogramMatrix(imageMatrix);
    }

    private static double[][] getHistogramMatrix(int[][][] imageMatrix) {
        int n = imageMatrix.length * imageMatrix[0].length;
        double[] redData = new double[n];
        double[] greenData = new double[n];
        double[] blueData = new double[n];

        for (int y = 0; y < imageMatrix.length; y++) {
            for (int x = 0; x < imageMatrix[y].length; x++) {
                int index = y * imageMatrix[y].length + x;
                redData[index] = imageMatrix[y][x][0];
                greenData[index] = imageMatrix[y][x][1];
                blueData[index] = imageMatrix[y][x][2];
            }
        }
        return new double[][]{redData, greenData, blueData};
    }

    public static int[][] getIntHistogram(int[][][] imageMatrix) {
        int[][] histogram = new int[3][256];
        for (int[][] row : imageMatrix) {
            for (int[] pixel : row) {
                for (int k = 0; k < 3; k++) {
                    histogram[k][pixel[k]]++;
                }
            }
        }
        return histogram;
    }

    public static void stretching(PortableAnymap image) {
        int[][][] imageMatrix = image.getMatrix();
        int[][] histogram = getIntHistogram(imageMatrix);
        for (int i = 0; i < 3; i++) {
            stretchingChannel(imageMatrix, i, getMin(histogram[i]), getMax(histogram[i]));
        }
        image.updateImage();
    }


    private static void stretchingChannel(int[][][] imageMatrix, int channel, int min, int max) {
        double range = max - min;
        if (max != 255 || min != 0) {
            for (int i = 0; i < imageMatrix.length; i++) {
                for (int j = 0; j < imageMatrix[i].length; j++) {
                    int value = imageMatrix[i][j][channel];
                    double newValue = ((value - min) * (255 / range));
                    imageMatrix[i][j][channel] = (int) Math.max(0, Math.min(255, newValue));
                }
            }
        }
    }


    public static void equalization(PortableAnymap image) {
        int[][][] imageMatrix = image.getMatrix();
        int[][] histogram = getIntHistogram(imageMatrix);
        int totalPixels = image.getHeight() * image.getWidth();
        double[] cumulativeProbability = new double[256];


        cumulativeProbability[0] = (double) histogram[0][0] / totalPixels;
        for (int i = 1; i < 256; i++) {
            cumulativeProbability[i] = cumulativeProbability[i - 1] + (double) histogram[0][i] / totalPixels;
        }

        for (int i = 0; i < imageMatrix.length; i++) {
            for (int j = 0; j < imageMatrix[i].length; j++) {
                for (int k = 0; k < 3; k++) {
                    int oldIntensity = imageMatrix[i][j][k];
                    int newIntensity = (int) (255 * cumulativeProbability[oldIntensity]);
                    imageMatrix[i][j][k] = Math.max(0, Math.min(255, newIntensity));
                }
            }
        }

        image.updateImage();
    }


    private static int getMin(int[] histogram) {
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] > 0) {
                return i;
            }
        }
        return 0;
    }

    private static int getMax(int[] histogram) {
        for (int i = histogram.length - 1; i >= 0; i--) {
            if (histogram[i] > 0) {
                return i;
            }
        }
        return 0;
    }
}
