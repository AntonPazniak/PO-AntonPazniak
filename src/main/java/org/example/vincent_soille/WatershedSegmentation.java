package org.example.vincent_soille;

import org.example.canny.CannyEdgeDetector;
import org.example.filters.SobelFilter;
import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class WatershedSegmentation {

    public static void convert(PortableAnymap image) {
        int[][] markers = createMarkers(CannyEdgeDetector.convert(image.copyMatrix()));

        int[][] watershedResult = applyWatershed(image.copyMatrix(), markers);

        image.setMatrix(visualizeResult(image.getMatrix(), watershedResult));
    }

    public static int[][][] visualizeResult(int[][][] imageMatrix, int[][] watershedResult) {
        int height = imageMatrix.length;
        int width = imageMatrix[0].length;

        int[][][] resultImage = new int[height][width][3];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int label = watershedResult[i][j];
                if (label == 0) {
                    resultImage[i][j] = new int[]{255, 0, 0};
                } else {
                    resultImage[i][j] = new int[]{label * 50 % 256, label * 80 % 256, label * 100 % 256};

                }
            }
        }
        return resultImage;
    }

    public static int[][] createMarkers(int[][][] binary) {
        int height = binary.length;
        int width = binary[0].length;
        int[][] markers = new int[height][width];
        int marker = 1;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (binary[i][j][0] == 255 && markers[i][j] == 0) {
                    floodFill(binary, markers, i, j, marker);
                    marker++;
                }
            }
        }
        return markers;
    }

    private static void floodFill(int[][][] binary, int[][] markers, int x, int y, int marker) {
        int height = binary.length;
        int width = binary[0].length;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        markers[x][y] = marker;

        while (!queue.isEmpty()) {
            int[] point = queue.poll();
            int px = point[0];
            int py = point[1];

            for (int i = 0; i < 4; i++) {
                int nx = px + dx[i];
                int ny = py + dy[i];

                if (nx >= 0 && ny >= 0 && nx < height && ny < width && binary[nx][ny][0] == 255 && markers[nx][ny] == 0) {
                    markers[nx][ny] = marker;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    }

    public static int[][] applyWatershed(int[][][] image, int[][] markers) {
        int height = image.length;
        int width = image[0].length;
        int[][] labels = new int[height][width];
        int[][] gradient = SobelFilter.computeGradient2D(Desaturation.convertToGrayscale2D(image));

        Queue<int[]> queue = new PriorityQueue<>(Comparator.comparingInt(p -> gradient[p[0]][p[1]]));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (markers[i][j] > 0) {
                    queue.add(new int[]{i, j});
                    labels[i][j] = markers[i][j];
                } else {
                    labels[i][j] = -1;
                }
            }
        }

        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] point = queue.poll();
            int x = point[0];
            int y = point[1];
            int currentLabel = labels[x][y];

            for (int k = 0; k < 4; k++) {
                int nx = x + dx[k];
                int ny = y + dy[k];

                if (nx >= 0 && ny >= 0 && nx < height && ny < width) {
                    if (labels[nx][ny] == -1) {
                        labels[nx][ny] = currentLabel;
                        queue.add(new int[]{nx, ny});
                    } else if (labels[nx][ny] > 0 && labels[nx][ny] != currentLabel) {
                        labels[nx][ny] = 0; // Граница водораздела
                    }
                }
            }
        }

        return labels;
    }

}
