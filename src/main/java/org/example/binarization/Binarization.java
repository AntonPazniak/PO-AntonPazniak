package org.example.binarization;

import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Binarization {

    public static void simpleBinarization(PortableAnymap image, int minPixel, int maxPixel) {
        Desaturation.convert(image);

        int[][][] imageMatrix = image.getMatrix();
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                int pixelValue = imageMatrix[x][y][0];
                if (pixelValue >= minPixel && pixelValue <= maxPixel) {
                    imageMatrix[x][y] = new int[]{255, 255, 255};
                } else {
                    imageMatrix[x][y] = new int[]{0, 0, 0};
                }
            }
        }
        image.updateImage();
    }

    public static int[][][] simpleBinarization(int[][][] imageMatrix, int minPixel, int maxPixel) {
        int[][][] newImageMatrix = new int[imageMatrix.length][imageMatrix[0].length][3];
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                int pixelValue = imageMatrix[x][y][0];
                if (pixelValue >= minPixel && pixelValue <= maxPixel) {
                    newImageMatrix[x][y] = new int[]{255, 255, 255};
                } else {
                    newImageMatrix[x][y] = new int[]{0, 0, 0};
                }
            }
        }
        return newImageMatrix;
    }

    public static void otsuBinarization(PortableAnymap image) {
        Histogram.stretching(image);
        Histogram.equalization(image);
        Desaturation.convert(image);
        int[][] histogram = Histogram.getIntHistogram(image.getMatrix());
        int threshold = calculateOtsuThreshold(image.getMatrix(), histogram[0]);
        applyThreshold(image, threshold);
        image.updateImage();
    }

    public static int getOtsuThreshold(int[][][] imageMatrix) {
        int[][] histogram = Histogram.getIntHistogram(imageMatrix);
        return calculateOtsuThreshold(imageMatrix, histogram[0]);
    }


    @Contract(pure = true)
    private static int calculateOtsuThreshold(@NotNull int[][][] imageMatrix, int[] histogram) {
        int totalPixels = imageMatrix.length * imageMatrix[0].length;

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;
        float maxVariance = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;

            wF = totalPixels - wB;
            if (wF == 0) break;

            sumB += i * histogram[i];

            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varianceBetween = (float) wB * wF * (mB - mF) * (mB - mF);

            if (varianceBetween > maxVariance) {
                maxVariance = varianceBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private static void applyThreshold(@NotNull PortableAnymap image, int threshold) {
        System.out.println(threshold);
        int[][][] imageMatrix = image.getMatrix();
        for (int y = 0; y < imageMatrix.length; y++) {
            for (int x = 0; x < imageMatrix[y].length; x++) {
                int pixelValue = imageMatrix[y][x][0];
                var binaryValue = (pixelValue < threshold) ? new int[]{0, 0, 0} : new int[]{255, 255, 255};
                imageMatrix[y][x] = binaryValue;

            }
        }
    }


}
