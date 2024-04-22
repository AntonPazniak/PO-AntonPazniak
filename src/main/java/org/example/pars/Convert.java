package org.example.pars;

import org.example.models.PortableAnymap;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.example.pars.CreateImageFromMatrix.createImageFromRGBMatrix;

public final class Convert {

    public static PortableAnymap open(String s){
        try {
            return convertFileToMatrix(convertFileToString(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean save(PortableAnymap portableAnymap){
        return switch (portableAnymap.getHead()) {
            case "P1" -> saveP1(portableAnymap);
            case "P2" -> saveP2(portableAnymap);
            case "P3" -> saveP3(portableAnymap);
            default -> false;
        };
    }


    private static String convertStringToAscii(String rawString) {
        byte[] byteArray = rawString.getBytes(StandardCharsets.US_ASCII);
        return new String(byteArray, StandardCharsets.US_ASCII);
    }


    private static String readLine(DataInputStream dataInputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c;
        while ((c = (char) dataInputStream.readByte()) != '\n') {
            if (c != '\r') {
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }


    private static String convertFileToString(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder result = new StringBuilder();
            int countLinesSkip = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '#') {
                    countLinesSkip++;
                }
                line = line.replaceAll("#.*", "");
                if (!line.trim().isEmpty()) {
                    result.append(line.trim()).append(" ");
                }
            }
            String finaleResult = convertStringToAscii(String.valueOf(result)).trim();
            String[] elements = finaleResult.split(" ");
            if (elements[0].equals("P4") || elements[0].equals("P5") || elements[0].equals("P6")) {
                countLinesSkip += 2;
                StringBuilder sb = new StringBuilder();
                sb.append(elements[0]).append(" ");
                sb.append(elements[1]).append(" ").append(elements[2]).append(" ");
                if (!elements[0].equals("P4")) {
                    sb.append(elements[3]).append(" ");
                    countLinesSkip++;
                }
                result = convertRawToString(filePath, sb, countLinesSkip);
            }
            return convertStringToAscii(String.valueOf(result)).trim();
        }
    }

    private static StringBuilder convertRawToString(String filePath, StringBuilder result, int countToSkip) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {
            for (int i = 0; i < countToSkip; i++) {
                readLine(dataInputStream);
            }
            while (dataInputStream.available() > 0) {
                int byteValue = dataInputStream.readUnsignedByte();
                result.append(byteValue).append(" ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static PortableAnymap convertFileToMatrix(String s){
        String[] elements = s.split(" ");
        int i;
        PortableAnymap portableAnymap = new PortableAnymap();
        portableAnymap.setHead(elements[0]);

        portableAnymap.setHeight(Integer.parseInt(elements[2]));
        portableAnymap.setWidth(Integer.parseInt(elements[1]));
        switch (elements[0]) {
            case "P1", "P4" -> convertP1(portableAnymap, s);
            case "P2", "P5" -> convertP2(portableAnymap, s);
            case "P3", "P6" -> {
                portableAnymap.setColor(Integer.parseInt(elements[3]));
                convertP3(portableAnymap, s);
            }
        }
        return portableAnymap;
    }


    private static void convertP1(PortableAnymap portableAnymap, String s){
        StringBuilder result = new StringBuilder();
        String[] elements = s.split(" ");
        int [][][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()][3];
        for (int i = 3; i < elements.length; i++) {
            result.append(elements[i]);
        }

        char[] characters = result.toString().toCharArray();
        int i = 0;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                if (characters[i] == '1')
                    matrix[x][y] = new int[] {0,0,0};
                else
                    matrix[x][y] = new int[] {255,255,255};
                i= i+1;
            }
        }
        portableAnymap.setMatrix(matrix);

        portableAnymap.setImage(createImageFromRGBMatrix(matrix));
    }

    private static void convertP2(PortableAnymap portableAnymap, String s){
        String[] elements = s.split(" ");
        int [][][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()][3];
        portableAnymap.setColor(Integer.parseInt(elements[3]));
        int i = 4;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = new int[] {Integer.parseInt(elements[i]),Integer.parseInt(elements[i]),Integer.parseInt(elements[i])};
                i= i+1;
            }
        }
        portableAnymap.setMatrix(matrix);

        portableAnymap.setImage(createImageFromRGBMatrix(matrix));
    }

    private static void convertP3(PortableAnymap portableAnymap, String s){
        String[] elements = s.split(" ");
        int [][][] matrix = new int[portableAnymap.getHeight()][portableAnymap.getWidth()][3];
        int i = 4;
        for(int x = 0; x< portableAnymap.getHeight(); x++){
            for(int y = 0; y< portableAnymap.getWidth(); y++){
                matrix[x][y] = new int[] {Integer.parseInt(elements[i]),Integer.parseInt(elements[i+1]),Integer.parseInt(elements[i+2])};
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
        p1.append(portableAnymap.getWidth()).append(" ").append(portableAnymap.getHeight()).append("\n");
        int n = 0;
        for(int[][] i : portableAnymap.getMatrix()){
            for(int[] j : i){
                int color;
                if(j[2] == 255)
                    color = 1;
                else
                    color = 0;
                if(n < 70){
                    n++;
                    p1.append(color);
                }else{
                    n = 1;
                    p1.append("\n").append(color);
                }
            }
        }

        portableAnymap.setContent(String.valueOf(p1));
        return writeFile(portableAnymap);
    }

    private static Boolean saveP2(PortableAnymap portableAnymap){
        StringBuilder p1 = new StringBuilder();
        p1.append(portableAnymap.getHead()).append("\n");
        p1.append(portableAnymap.getWidth()).append(" ").append(portableAnymap.getHeight()).append("\n");
        p1.append(portableAnymap.getColor()).append("\n");
        for(int[][] i : portableAnymap.getMatrix()){
            for(int [] j : i){
                p1.append(j[0]).append("\n");
            }
        }

        portableAnymap.setContent(String.valueOf(p1));
        return writeFile(portableAnymap);
    }

    private static Boolean saveP3(PortableAnymap portableAnymap){
        StringBuilder p1 = new StringBuilder();
        p1.append(portableAnymap.getHead()).append("\n");
        p1.append(portableAnymap.getWidth()).append(" ").append(portableAnymap.getHeight()).append("\n");
        p1.append(portableAnymap.getColor()).append("\n");
        for(int[][] i : portableAnymap.getMatrix()){
            for(int [] j : i){
                p1.append(j[0]).append("\n");
                p1.append(j[1]).append("\n");
                p1.append(j[2]).append("\n");
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
