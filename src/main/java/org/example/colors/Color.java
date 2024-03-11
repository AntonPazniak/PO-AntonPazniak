package org.example.colors;

public class Color {

    public static float[] rgbToHsl(int[] colors) {
        float r = colors[0] / 255f;
        float g = colors[1] / 255f;
        float b = colors[2] / 255f;

        float max = Math.max(Math.max(r, g), b);
        float min = Math.min(Math.min(r, g), b);

        float hue, saturation, lightness = (max + min) / 2;

        if (max == min) {
            hue = saturation = 0; // achromatic
        } else {
            float d = max - min;
            saturation = lightness > 0.5 ? d / (2 - max - min) : d / (max + min);

            if (max == r) {
                hue = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                hue = (b - r) / d + 2;
            } else {
                hue = (r - g) / d + 4;
            }

            hue /= 6;
        }

        return new float[]{hue, saturation, lightness};
    }

    // HSL to RGB
    public static int[] hslToRgb(float[] color) {
        float r, g, b;

        if (color[1] == 0) {
            r = g = b = color[2]; // achromatic
        } else {
            float q = color[2] < 0.5 ? color[2] * (1 + color[1]) : color[2] + color[1] - color[2] * color[1];
            float p = 2 * color[2] - q;
            r = hueToRgb(p, q, color[0] + 1 / 3f);
            g = hueToRgb(p, q, color[0]);
            b = hueToRgb(p, q, color[0] - 1 / 3f);
        }

        return new int[]{(int) (r * 255), (int) (g * 255), (int) (b * 255)};
    }

    private static float hueToRgb(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1 / 6f) return p + (q - p) * 6 * t;
        if (t < 1 / 2f) return q;
        if (t < 2 / 3f) return p + (q - p) * (2 / 3f - t) * 6;
        return p;
    }

    public static void main(String[] args) {
        // Example: RGB to HSL
//        int red = 128;
//        int green = 64;
//        int blue = 255;
//
//        float[] hslValues = rgbToHsl(red, green, blue);
//        System.out.println("RGB to HSL: Hue=" + hslValues[0] + ", Saturation=" + hslValues[1] + ", Lightness=" + hslValues[2]);
//
//        // Example: HSL to RGB
//        float hue = 0.7f;
//        float saturation = 0.8f;
//        float lightness = 0.4f;
//
//        int[] rgbValues = hslToRgb(hslValues[0], hslValues[1], hslValues[2]);
//        System.out.println("H`SL to RGB: Red=" + rgbValues[0] + ", Green=" + rgbValues[1] + ", Blue=" + rgbValues[2]);
    }

}
