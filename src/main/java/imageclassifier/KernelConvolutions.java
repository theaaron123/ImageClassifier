package imageclassifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Created by aaron on 14/11/16.
 */
public class KernelConvolutions {

    public BufferedImage gaussianBlur(BufferedImage originalImage, BufferedImage blurredImage) {

        float[] kernel = new float[]
                {1 / 16f, 1 / 8f, 1 / 16f,
                        1 / 8f, 1 / 4f, 1 / 8f,
                        1 / 16f, 1 / 8f, 1 / 16f};

        BufferedImageOp operation = new ConvolveOp(new Kernel(3, 3, kernel));
        return operation.filter(originalImage, blurredImage);
    }

    public BufferedImage meanBlur(BufferedImage originalImage, BufferedImage blurredImage) {
        float[] kernel = new float[]
                {1, 1, 1,
                        1, 1, 1,
                        1, 1, 1};
        BufferedImageOp operation = new ConvolveOp(new Kernel(3, 3, kernel));
        return operation.filter(originalImage, blurredImage);
    }

    public BufferedImage sobelOperator(BufferedImage originalImage, BufferedImage sobelImage) {
        float[] kernelx = new float[]
                {-1, 0, 1,
                        -2, 0, 2,
                        -1, 0, 1};

        float[] kernely = new float[]
                {-1, -2, -1,
                        0, 0, 0,
                        1, 2, 1};


        BufferedImageOp operationx = new ConvolveOp(new Kernel(3, 3, kernelx));
        BufferedImageOp operationy = new ConvolveOp(new Kernel(3, 3, kernely));

        sobelOperatorReal(originalImage);
        operationx.filter(originalImage, sobelImage);
        operationy.filter(originalImage, sobelImage);
        // Only works in one direction...
        return sobelImage;
    }

    public double[][] sobelOperatorReal(BufferedImage originalImage) {

        int[] grayData = new int[originalImage.getWidth() * originalImage.getHeight()];

        for (int y = 0; y < originalImage.getHeight(); y++) {
            for (int x = 0; x < originalImage.getWidth(); x++) {
                grayData[y * originalImage.getWidth() + x] = originalImage.getRGB(x, y);
            }
        }

        int[] buffer = new int[9];
        double[] magnitude = new double[originalImage.getWidth() * originalImage.getHeight()];
        double[] orientation = new double[originalImage.getWidth() * originalImage.getHeight()];

        for (int y = 1; y < originalImage.getHeight() - 1; y++) {
            for (int x = 1; x < originalImage.getWidth() - 1; x++) {
                int index = y * originalImage.getWidth() + x;

                buffer[0] = grayData[index - originalImage.getWidth() - 1];
                buffer[1] = grayData[index - originalImage.getWidth()];
                buffer[2] = grayData[index - originalImage.getWidth() + 1];
                buffer[3] = grayData[index - 1];
                buffer[4] = grayData[index];
                buffer[5] = grayData[index + 1];
                buffer[6] = grayData[index + originalImage.getWidth() - 1];
                buffer[7] = grayData[index + originalImage.getWidth()];
                buffer[8] = grayData[index + originalImage.getWidth() + 1];

                double dx = buffer[2] + 2 * buffer[5] + buffer[8] - buffer[0] - 2 * buffer[3] - buffer[6];
                double dy = buffer[6] + 2 * buffer[7] + buffer[8] - buffer[0] - 2 * buffer[1] - buffer[2];

                magnitude[index] = Math.sqrt((dx * dx) + (dy * dy)) / 1143.0;
                orientation[index] = Math.atan2(dy, dx) + Math.PI;
            }
        }

        double[] widthHeight = new double[2];
        widthHeight[0] = originalImage.getWidth();
        widthHeight[1] = originalImage.getHeight();
        double[][] magOrien = new double[3][];
        magOrien[0] = magnitude;
        magOrien[1] = orientation;
        magOrien[2] = widthHeight;
        return magOrien;
    }

    public BufferedImage imageFromMagnitudeOrientation(double[] magnitude, double[] orientation, int width, int height) {
        List<Double> list = new ArrayList<Double>();
        for (int i = 0; i < magnitude.length; i++) {
            list.add(magnitude[i]);
        }
        double max = Collections.max(list);

        int[] out = new int[magnitude.length];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                int rgb = Color.HSBtoRGB((float) orientation[index], 0, (float) (magnitude[index] / max));
                out[index] = rgb;
            }
        }

        try {
            BufferedImage output = getImageFromArray("test1.jpg", out, width, height);
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public double magnitudeWhiteCountRatio(double[] magnitude) {
        double count = 0;
        for (double d : magnitude) {
            if (d != 0) {
                count++;
            }
        }
        return count / magnitude.length;
    }

    public static BufferedImage getImageFromArray(String filename, int pixels[], int width, int height) throws IOException {
        BufferedImage image = null;
        WritableRaster raster = null;

        if (image == null) {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            raster = image.getRaster();
        }

        raster.setPixels(0, 0, width, height, pixels);
        try {
            ImageIO.write(image, "jpg", new FileOutputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
