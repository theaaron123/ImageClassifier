package imageclassifier;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

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

        operationx.filter(originalImage, sobelImage);
        operationy.filter(sobelImage, originalImage);

        return originalImage;
    }
}
