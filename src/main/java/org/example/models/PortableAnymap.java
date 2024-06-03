package org.example.models;


import lombok.*;
import org.example.pars.CreateImageFromMatrix;

import java.awt.*;
import java.io.File;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortableAnymap {

    private Image image;
    private int [][][] matrix;
    private int height;
    private int Width;
    private int color;
    private String head;
    private File file;
    private String content;

    public void updateImage() {
        image = CreateImageFromMatrix.createImageFromRGBMatrix(matrix);
    }

    public int[][][] copyMatrix() {
        int[][][] copy = new int[matrix.length][][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = new int[matrix[i].length][];
            for (int j = 0; j < matrix[i].length; j++) {
                copy[i][j] = new int[matrix[i][j].length];
                System.arraycopy(matrix[i][j], 0, copy[i][j], 0, matrix[i][j].length);
            }
        }
        return copy;
    }

    public int[][][] copyMatrix(int[][][] matrix) {
        int[][][] copy = new int[matrix.length][][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = new int[matrix[i].length][];
            for (int j = 0; j < matrix[i].length; j++) {
                copy[i][j] = new int[matrix[i][j].length];
                System.arraycopy(matrix[i][j], 0, copy[i][j], 0, matrix[i][j].length);
            }
        }
        return copy;
    }

    public void setMatrix(int[][][] matrix) {
        this.matrix = matrix;
        updateImage();
    }
}
