import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class PgmFileReader {

    public static void main(String[] args) {
        String filePath = "src/main/resources/test_raw.ppm";

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {

            System.out.println(readLine(dataInputStream));
            System.out.println(readLine(dataInputStream));
            System.out.println(readLine(dataInputStream));
            System.out.println(readLine(dataInputStream));

            while (dataInputStream.available() > 0) {
                int byteValue = dataInputStream.readUnsignedByte();
                System.out.println(byteValue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
}

