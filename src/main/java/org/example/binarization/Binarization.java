package org.example.binarization;

import org.example.models.PortableAnymap;
import org.example.point_operation.Desaturation;
import org.example.point_operation.PointFilter;

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


}
