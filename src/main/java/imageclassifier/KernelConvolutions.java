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
}
