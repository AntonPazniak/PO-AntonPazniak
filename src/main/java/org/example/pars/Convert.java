package org.example.pars;

import org.example.models.PortableAnymap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Convert {

    public static PortableAnymap open(String s){
        try {
            return convertFileToMatrix(convertFileToString(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean save(PortableAnymap portableAnymap){
        if(portableAnymap.getHead().equals("P1")){
            saveP1(portableAnymap);
        }
        return false;
    }


    private static String convertStringToAscii(String rawString) {
        byte[] byteArray = rawString.getBytes(StandardCharsets.US_ASCII);
        return new String(byteArray, StandardCharsets.US_ASCII);
    }


    private static String convertFileToString(String ppmFilePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(ppmFilePath))) {
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                // Удаляем строки с символом '#'
                line = line.replaceAll("#.*", "");

                // Пропускаем пустые строки
                if (!line.trim().isEmpty()) {
                    // Объединяем символы в одну строку, разделяя пробелами
                    result.append(line.trim()).append(" ");
                }
            }

            return convertStringToAscii(String.valueOf(result)).trim();
        }
    }

    private static PortableAnymap convertFileToMatrix(String s){
        String[] elements = s.split(" ");
        int i;
        PortableAnymap portableAnymap = new PortableAnymap();
        portableAnymap.setHead(elements[0]);

        portableAnymap.setHeight(Integer.parseInt(elements[1]));
        portableAnymap.setWidth(Integer.parseInt(elements[2]));
        if(elements[0].equals("P1") || elements[0].equals("P4")){
            convertP1(portableAnymap, s);
        } else if(elements[0].equals("P2") || elements[0].equals("P5")){
            convertP2(portableAnymap, s);
        }else if(elements[0].equals("P3") || elements[0].equals("P6")){
            portableAnymap.setColor(Integer.parseInt(elements[3]));
            convertP3(portableAnymap,s);
        }
        return portableAnymap;
    }


    private static void convertP1(PortableAnymap portableAnymap, String s){
        StringBuilder result = new StringBuilder();
        String[] elements = s.split(" ");
        int [][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        for (int i = 3; i < elements.length; i++) {
            result.append(elements[i]);
        }

        char[] characters = result.toString().toCharArray();
        int i = 0;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = Integer.parseInt(String.valueOf(characters[i]));
                i= i+1;
            }
        }
        portableAnymap.setMatrix(matrix);

        portableAnymap.setImage(createPBMImageFromBinaryMatrix(matrix));
    }

    private static void convertP2(PortableAnymap portableAnymap, String s){
        String[] elements = s.split(" ");
        int [][] matrix = new int[Integer.parseInt(elements[1])][Integer.parseInt(elements[2])];
        int i = 4;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = Integer.parseInt(elements[i]);
                i= i+1;
            }
        }
        portableAnymap.setMatrix(matrix);

        portableAnymap.setImage(createPGMImageFromRGBMatrix(matrix));
    }

    private static void convertP3(PortableAnymap portableAnymap, String s){
        String[] elements = s.split(" ");
        int [][] matrix = new int[Integer.parseInt(elements[1])][Integer.parseInt(elements[2])];
        int i = 4;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = getColorValue(Integer.parseInt(elements[i]),Integer.parseInt(elements[i+1]),Integer.parseInt(elements[i+2]));
                i= i+3;
            }
        }
        portableAnymap.setMatrix(matrix);
        portableAnymap.setImage(createImageFromRGBMatrix(matrix));
    }

    private static int getColorValue(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

    private static Image createImageFromRGBMatrix(int[][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = new Color(rgbMatrix[y][x]).getRGB();
                image.setRGB(x, y, rgb);
            }
        }

        return image;
    }

    public static BufferedImage createPBMImageFromBinaryMatrix(int[][] binaryMatrix) {
        int width = binaryMatrix[0].length;
        int height = binaryMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = (binaryMatrix[y][x] == 0) ? 0xFFFFFF : 0x000000;
                image.setRGB(x, y, pixelValue);
            }
        }

        return image;
    }

    public static BufferedImage createPGMImageFromRGBMatrix(int[][] rgbMatrix) {
        int width = rgbMatrix[0].length;
        int height = rgbMatrix.length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = rgbMatrix[y][x];
                int grayscale = (int) (0.299 * ((rgb >> 16) & 0xFF) + 0.587 * ((rgb >> 8) & 0xFF) + 0.114 * (rgb & 0xFF));
                int invertedGray = 255 - grayscale;
                int invertedGrayPixel = (invertedGray << 16) | (invertedGray << 8) | invertedGray;
                image.setRGB(x, y, invertedGrayPixel);
            }
        }

        return image;
    }

    private static Boolean saveP1(PortableAnymap portableAnymap){
        StringBuilder p1 = new StringBuilder();
        p1.append(portableAnymap.getHead()).append("\n");
        p1.append(portableAnymap.getHeight()).append(" ").append(portableAnymap.getWidth()).append("\n");
        int n = 0;
        for(int[] i : portableAnymap.getMatrix()){
            for(int j : i){
                if(n < 70){
                    n++;
                    p1.append(j);
                }else{
                    n = 1;
                    p1.append("\n").append(j);
                }
            }
        }

        portableAnymap.setContent(String.valueOf(p1));
        return writeFile(portableAnymap);
    }

    private static Boolean writeFile(PortableAnymap portableAnymap){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(portableAnymap.getFile()))) {
            // Ваш код для записи данных в файл
            writer.write(portableAnymap.getContent());

           return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при записи файла.");
        }
        return false;
    }

}
