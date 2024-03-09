package org.example.pars;

import org.example.models.PortableAnymap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.example.pars.CreateImageFromMatrix.*;

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
            return saveP1(portableAnymap);
        } else if (portableAnymap.getHead().equals("P2")) {
            return saveP2(portableAnymap);
        }else if (portableAnymap.getHead().equals("P3")) {
            return saveP3(portableAnymap);
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

        portableAnymap.setHeight(Integer.parseInt(elements[2]));
        portableAnymap.setWidth(Integer.parseInt(elements[1]));
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
        int [][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
        portableAnymap.setColor(Integer.parseInt(elements[3]));
        int i = 4;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = Integer.parseInt(elements[i]);
                i= i+1;
            }
        }
        portableAnymap.setMatrix(matrix);

        portableAnymap.setImage(createPPMImageFromRGBMatrix(matrix));
    }

    private static void convertP3(PortableAnymap portableAnymap, String s){
        String[] elements = s.split(" ");
        int [][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()];
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

    public static int getColorValue(int red, int green, int blue) {
        return (red << 16) | (green << 8) | blue;
    }

    public static int[] getRGBValues(int colorValue) {
        int red = (colorValue >> 16) & 0xFF;
        int green = (colorValue >> 8) & 0xFF;
        int blue = colorValue & 0xFF;

        return new int[]{red, green, blue};
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

    private static Boolean saveP2(PortableAnymap portableAnymap){
        StringBuilder p1 = new StringBuilder();
        p1.append(portableAnymap.getHead()).append("\n");
        p1.append(portableAnymap.getHeight()).append(" ").append(portableAnymap.getWidth()).append("\n");
        p1.append(portableAnymap.getColor()).append("\n");
        for(int[] i : portableAnymap.getMatrix()){
            for(int j : i){
                p1.append(j).append("\n");
            }
        }

        portableAnymap.setContent(String.valueOf(p1));
        return writeFile(portableAnymap);
    }

    private static Boolean saveP3(PortableAnymap portableAnymap){
        StringBuilder p1 = new StringBuilder();
        p1.append(portableAnymap.getHead()).append("\n");
        p1.append(portableAnymap.getHeight()).append(" ").append(portableAnymap.getWidth()).append("\n");
        p1.append(portableAnymap.getColor()).append("\n");
        for(int[] i : portableAnymap.getMatrix()){
            for(int j : i){
                int [] colors = getRGBValues(j);
                p1.append(colors[0]).append("\n");
                p1.append(colors[1]).append("\n");
                p1.append(colors[2]).append("\n");
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
