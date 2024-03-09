package org.example.models;


import java.awt.*;
import java.io.File;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class PortableAnymap {

    private Image image;
    private int [][][] matrix;
    private int height;
    private int Width;
    private int color;
    private String head;
    private File file;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public PortableAnymap() {
    }
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int[][][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][][] matrix) {
        this.matrix = matrix;
    }
}
