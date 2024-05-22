package org.example.harris;

import org.example.filters.GaussFilter;
import org.example.filters.SobelFilter;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

public class Harris {

    public static void convert(PortableAnymap image) {
        // Desaturate the image
        Desaturation.convert(image);

        // Apply Sobel filter to get gradients
        SobelFilter sobel = new SobelFilter();
        sobel.applySobel(image.copyMatrix());
        double[][][] sobelX = sobel.getImageMatrixSobelX();
        double[][][] sobelY = sobel.getImageMatrixSobelY();

        // Compute products of derivatives at each pixel
        double[][][] lXX = multiplyMatrices(sobelX, sobelX);
        double[][][] lYY = multiplyMatrices(sobelY, sobelY);
        double[][][] lXY = multiplyMatrices(sobelX, sobelY);

        // Apply Gaussian filter to the products
        double[][][] sXX = GaussFilter.convert(lXX, 7, 1.5);
        double[][][] sYY = GaussFilter.convert(lYY, 7, 1.5);
        double[][][] sXY = GaussFilter.convert(lXY, 7, 1.5);

        // Initialize variables for Harris response calculation
        int[][][] imageMatrix = image.getMatrix();
        double k = 0.04;
        double threshold = 1000; // Adjust this value as necessary
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] R = new double[height][width];

        // Compute Harris response R for each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double detM = sXX[y][x][0] * sYY[y][x][0] - sXY[y][x][0] * sXY[y][x][0];
                double traceM = sXX[y][x][0] + sYY[y][x][0];
                R[y][x] = detM - k * (traceM * traceM);
            }
        }

        // Apply non-maximum suppression and thresholding
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (R[y][x] > threshold &&
                        R[y][x] > R[y - 1][x - 1] && R[y][x] > R[y - 1][x] && R[y][x] > R[y - 1][x + 1] &&
                        R[y][x] > R[y][x - 1] && R[y][x] > R[y][x + 1] &&
                        R[y][x] > R[y + 1][x - 1] && R[y][x] > R[y + 1][x] && R[y][x] > R[y + 1][x + 1]) {

                    imageMatrix[y][x] = new int[]{255, 0, 0}; // Mark the corner in red
                }
            }
        }
        image.updateImage();
    }

    // Fix multiplyMatrices method to element-wise multiplication
    public static double[][][] multiplyMatrices(double[][][] firstMatrix, double[][][] secondMatrix) {
        int rows = firstMatrix.length;
        int columns = firstMatrix[0].length;

        // Create result matrix
        double[][][] resultMatrix = new double[rows][columns][3];

        // Element-wise multiplication of matrices
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                var pixel =  Math.min(250,Math.max(0,firstMatrix[i][j][0] * secondMatrix[i][j][0]));
                resultMatrix[i][j] = new double [] {pixel,pixel,pixel};
            }
        }
        return resultMatrix;
    }

}

