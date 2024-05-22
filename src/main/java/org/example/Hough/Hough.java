package org.example.Hough;

import org.example.canny.CannyEdgeDetector;
import org.example.models.PortableAnymap;

import java.util.Arrays;

public class Hough {

    private static final int[] whiteColor = new int[]{255, 0, 0};
    private static final int[] blackColor = new int[]{0, 0, 0};

    public static void convert(PortableAnymap image) {
        CannyEdgeDetector.convert(image);

        int[][][] originalMatrix = image.getMatrix();
        int width = image.getWidth();
        int height = image.getHeight();

        int maxRho = (int) Math.hypot(width, height);
        int[][] accumulator = new int[2 * maxRho][180];

        // Voting in the accumulator
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isWhite(originalMatrix[y][x])) {
                    for (int theta = 0; theta < 180; theta++) {
                        double thetaRad = Math.toRadians(theta);
                        int rho = (int) (x * Math.cos(thetaRad) + y * Math.sin(thetaRad));
                        accumulator[rho + maxRho][theta]++;
                    }
                }
            }
        }

        // Calculating the dynamic threshold
        int threshold = height/2;

        // Detecting lines by finding peaks in the accumulator
        for (int rho = 0; rho < 2 * maxRho; rho++) {
            for (int theta = 0; theta < 180; theta++) {
                if (accumulator[rho][theta] > threshold) {
                    drawLine(originalMatrix, rho - maxRho, theta);
                }
            }
        }

        image.setMatrix(originalMatrix);
    }

    private static boolean isWhite(int[] pixel) {
        return pixel[0] == 255 && pixel[1] == 255 && pixel[2] == 255;
    }

    private static void drawLine(int[][][] imageMatrix, int rho, int theta) {
        int width = imageMatrix[0].length;
        int height = imageMatrix.length;
        double thetaRad = Math.toRadians(theta);

        if (theta == 0 || theta == 180) {
            for (int y = 0; y < height; y++) {
                int x = rho;
                if (x >= 0 && x < width) {
                    imageMatrix[y][x] = whiteColor;
                }
            }
        } else if (theta == 90) {
            for (int x = 0; x < width; x++) {
                int y = rho;
                if (y >= 0 && y < height) {
                    imageMatrix[y][x] = whiteColor;
                }
            }
        } else {
            for (int x = 0; x < width; x++) {
                int y = (int) ((rho - x * Math.cos(thetaRad)) / Math.sin(thetaRad));
                if (y >= 0 && y < height) {
                    imageMatrix[y][x] = whiteColor;
                }
            }
            for (int y = 0; y < height; y++) {
                int x = (int) ((rho - y * Math.sin(thetaRad)) / Math.cos(thetaRad));
                if (x >= 0 && x < width) {
                    imageMatrix[y][x] = whiteColor;
                }
            }
        }
    }

    private static int calculateThreshold(int[][] accumulator) {
        int maxVote = Arrays.stream(accumulator).flatMapToInt(Arrays::stream).max().orElse(0);

        int[] histogram = new int[maxVote + 1];
        for (int[] row : accumulator) {
            for (int vote : row) {
                histogram[vote]++;
            }
        }

        int totalVotes = Arrays.stream(histogram).sum();
        int cumulativeVotes = 0;
        int threshold = maxVote;

        for (int vote = maxVote; vote > 0; vote--) {
            cumulativeVotes += histogram[vote];
            if (cumulativeVotes > totalVotes * 0.0001) {  // Adjust this factor to control sensitivity
                threshold = vote;
                break;
            }
        }

        return threshold;
    }
}