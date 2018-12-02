package de.pauhull.mapgen.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class JavaMap {

    public byte[] colors;
    public int width, height;

    public JavaMap(BufferedImage image, int width, int height) {

        BufferedImage scaledImage = resizeImage(image, width * 128, height * 128);

        this.colors = new byte[scaledImage.getWidth() * scaledImage.getHeight()];
        this.width = width;
        this.height = height;

        for (int x = 0; x < scaledImage.getWidth(); x++) {
            for (int y = 0; y < scaledImage.getHeight(); y++) {
                Color pixel = new Color(scaledImage.getRGB(x, y), true);
                Color nearest = new Color(0, 0, 0, 0);

                if (pixel.getAlpha() >= 128) {
                    double mindist = Double.POSITIVE_INFINITY;

                    for (Color color : ColorMap.colorMap.keySet()) {

                        double distance = ColorMap.colorDistance(pixel, color);

                        if (mindist > distance) {
                            mindist = distance;
                            nearest = color;
                        }
                    }

                    if (x < width - 1 && x > 0 && y < height - 1 && y > 0) {
                        int errR = pixel.getRed() - nearest.getRed();
                        int errG = pixel.getGreen() - nearest.getGreen();
                        int errB = pixel.getBlue() - nearest.getBlue();

                        // move error 7/16 to x+1, y+0
                        Color nextColor = new Color(image.getRGB(x + 1, y));
                        float r = nextColor.getRed();
                        float g = nextColor.getGreen();
                        float b = nextColor.getBlue();
                        r = r + errR * 7 / 16.0f;
                        g = g + errG * 7 / 16.0f;
                        b = b + errB * 7 / 16.0f;
                        r = clamp(r, 0, 255);
                        g = clamp(g, 0, 255);
                        b = clamp(b, 0, 255);
                        image.setRGB(x + 1, y, new Color((int) r, (int) g, (int) b).getRGB());

                        // move error 3/16 to x-1, y+1
                        nextColor = new Color(image.getRGB(x - 1, y + 1));
                        r = nextColor.getRed();
                        g = nextColor.getGreen();
                        b = nextColor.getBlue();
                        r = r + errR * 3 / 16.0f;
                        g = g + errG * 3 / 16.0f;
                        b = b + errB * 3 / 16.0f;
                        r = clamp(r, 0, 255);
                        g = clamp(g, 0, 255);
                        b = clamp(b, 0, 255);
                        image.setRGB(x - 1, y + 1, new Color((int) r, (int) g, (int) b).getRGB());

                        // move error 5/16 to x+0, y+1
                        nextColor = new Color(image.getRGB(x, y + 1));
                        r = nextColor.getRed();
                        g = nextColor.getGreen();
                        b = nextColor.getBlue();
                        r = r + errR * 5 / 16.0f;
                        g = g + errG * 5 / 16.0f;
                        b = b + errB * 5 / 16.0f;
                        r = clamp(r, 0, 255);
                        g = clamp(g, 0, 255);
                        b = clamp(b, 0, 255);
                        image.setRGB(x, y + 1, new Color((int) r, (int) g, (int) b).getRGB());

                        // move error 1/16 to x+1, y+1
                        nextColor = new Color(image.getRGB(x + 1, y + 1));
                        r = nextColor.getRed();
                        g = nextColor.getGreen();
                        b = nextColor.getBlue();
                        r = r + errR * 1 / 16.0f;
                        g = g + errG * 1 / 16.0f;
                        b = b + errB * 1 / 16.0f;
                        r = clamp(r, 0, 255);
                        g = clamp(g, 0, 255);
                        b = clamp(b, 0, 255);
                        image.setRGB(x + 1, y + 1, new Color((int) r, (int) g, (int) b).getRGB());
                    }
                }

                //System.out.println(nearest.getRed() + " " + nearest.getGreen() + " " + nearest.getBlue() + " " + nearest.getAlpha());

                if (nearest.getAlpha() == 0) {
                    colors[x + y * scaledImage.getWidth()] = 0x00;
                } else {
                    colors[x + y * scaledImage.getWidth()] = ColorMap.colorMap.get(nearest);
                }

            }
        }
    }

    public static float clamp(float x, float min, float max) {
        return Math.min(Math.max(x, min), max);
    }

    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage scaled = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D graphics = scaled.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        return scaled;
    }

}
