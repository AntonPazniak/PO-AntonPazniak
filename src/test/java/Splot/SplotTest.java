package Splot;

import org.example.filters.Splot;
import org.example.models.PortableAnymap;
import org.junit.jupiter.api.Test;

public class SplotTest {

    int[] b = new int[]{0, 0, 0};
    int[] w = new int[]{255, 255, 255};

    @Test
    public void testSobelOperation() {
        var imageMatrix = new int[][][]{
                {b, b, w, w, w, b},
                {b, b, w, w, w, b},
                {b, b, w, w, w, b},
                {b, b, w, w, w, b},
                {b, b, w, w, w, b}
        };
        PortableAnymap image = new PortableAnymap();
        image.setMatrix(imageMatrix);
        Splot.sobel(image);
        var angle = Splot.getAngelMatrix();


    }
}
