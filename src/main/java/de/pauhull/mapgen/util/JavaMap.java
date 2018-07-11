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
