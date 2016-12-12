package imageclassifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by aaron on 13/11/16.
 */
public class ImageClassifier {

    public Color classifyPixelColour(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);

        float hsb[] = new float[3];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;
        Color.RGBtoHSB(r, g, b, hsb);

        if (hsb[1] < 0.1 && hsb[2] > 0.9) return Color.WHITE;
        else if (hsb[2] < 0.1) return Color.BLACK;
        else {
            float deg = hsb[0] * 360;
            if (deg >= 0 && deg < 30) return Color.RED;
            else if (deg >= 30 && deg < 90) return Color.YELLOW;
            else if (deg >= 90 && deg < 150) return Color.GREEN;
            else if (deg >= 150 && deg < 210) return Color.CYAN;
            else if (deg >= 210 && deg < 270) return Color.BLUE;
            else if (deg >= 270 && deg < 330) return Color.MAGENTA;
            else return Color.RED;
        }
    }

    public BufferedImage getBufferedImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getPixelRGB(BufferedImage image, int x, int y) {
        return image.getRGB(x, y);
    }

    public void calculateImageColourComplexity(String path) {
        BufferedImage image = getBufferedImage(path);
        Set<Integer> colours = new HashSet<Integer>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Integer c = getPixelRGB(image, i, j);
                colours.add(c);
            }
        }
        System.out.println("size: " + colours.size());
    }

    public void calculateImageColourCount(String path) {
        BufferedImage image = getBufferedImage(path);
        Set<Color> colours = new HashSet<Color>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color c = classifyPixelColour(image, i, j);
                colours.add(c);
            }
        }
        System.out.println("size: " + colours.size());
    }

    public void gaussianBlurImage(String path) {
        BufferedImage image = getBufferedImage(path);
        BufferedImage output = deepCopy(image);
        KernelConvolutions kernelConvolutions = new KernelConvolutions();
        BufferedImage b = kernelConvolutions.gaussianBlur(image, output);
        File outputFile = new File("image.jpg");
        try {
            ImageIO.write(b, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage imageToGrayScale(String path) {
        BufferedImage image = getBufferedImage(path);
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp operator = new ColorConvertOp(image.getColorModel().getColorSpace(), output.getColorModel().getColorSpace(), null);
        operator.filter(image, output);
        return output;
    }

    static void writeImage(String name, BufferedImage image) {
        File outputFile = new File(name);
        try {
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPreMultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPreMultiplied, null);
    }
}