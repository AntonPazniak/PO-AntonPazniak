package org.example.binarization;

import org.example.histogram.Histogram;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public final class Binarization {

    public static void simpleBinarization(PortableAnymap image, int minPixel, int maxPixel) {
        PointFilter pointFilter = new Desaturation();
        pointFilter.convert(image);

        int[][][] imageMatrix = image.getMatrix();
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[x].length; y++) {
                int pixelValue = imageMatrix[x][y][0];
                if (pixelValue >= minPixel && pixelValue <= maxPixel) {
                    imageMatrix[x][y][0] = 255;
                    imageMatrix[x][y][1] = 255;
                    imageMatrix[x][y][2] = 255;
                } else {
                    imageMatrix[x][y][0] = 0;
                    imageMatrix[x][y][1] = 0;
                    imageMatrix[x][y][2] = 0;
                }
            }
        }
        image.updateImage();
    }

    public static void otsuBinarization(PortableAnymap image) {
        Histogram.stretching(image);
        Histogram.equalization(image);
        PointFilter pointFilter = new Desaturation();
        pointFilter.convert(image);
        int[][] histogram = Histogram.getIntHistogram(image.getMatrix());
        int threshold = calculateOtsuThreshold(image, histogram[0]);
        applyThreshold(image, threshold);
        image.updateImage();
    }


    private static int calculateOtsuThreshold(PortableAnymap image, int[] histogram) {
        int totalPixels = image.getWidth() * image.getHeight();
        PointFilter pointFilter = new Desaturation();
        pointFilter.convert(image);

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += (i * histogram[i]);
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

            sumB += (i * histogram[i]);

            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varianceBetween = wB * wF * (mB - mF) * (mB - mF);

            if (varianceBetween > maxVariance) {
                maxVariance = varianceBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private static void applyThreshold(PortableAnymap image, int threshold) {

        int[][][] imageMatrix = image.getMatrix();
        for (int y = 0; y < imageMatrix.length; y++) {
            for (int x = 0; x < imageMatrix[y].length; x++) {
                int pixelValue = imageMatrix[y][x][0];
                int binaryValue = (pixelValue < threshold) ? 0 : 255;
                for (int z = 0; z < 3; z++) {
                    imageMatrix[y][x][z] = binaryValue;
                }
            }
        }
    }


}
