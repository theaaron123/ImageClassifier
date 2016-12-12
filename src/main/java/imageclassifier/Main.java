package imageclassifier;

import java.awt.image.BufferedImage;

/**
 * Created by aaron on 13/11/16.
 */
public class Main {

    public static void main(String[] args) {
        ImageClassifier imageClassifier = new ImageClassifier();
        imageClassifier.calculateImageColourComplexity("image271.jpg");
        imageClassifier.calculateImageColourComplexity("image295.jpg");
        imageClassifier.calculateImageColourComplexity("image425.jpg");
        imageClassifier.calculateImageColourComplexity("image1490.jpg");
        imageClassifier.gaussianBlurImage("image1490.jpg");
        BufferedImage buff = imageClassifier.imageToGrayScale("image1491.jpg");
        ImageClassifier.writeImage("gray.jpg", buff);
    }
}
