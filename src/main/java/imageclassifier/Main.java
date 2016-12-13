package imageclassifier;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by aaron on 13/11/16.
 */
public class Main {

    public static void main(String[] args) {
        ImageClassifier imageClassifier = new ImageClassifier();

        File[] files = new File("/home/aaron/IdeaProjects/ImageData").listFiles();

        for (File file : files) {
            double sobelImageWhiteCountRatio = imageClassifier.sobelImageWhiteCountRatio(imageClassifier.imageToGrayScale(imageClassifier.gaussianBlurImage(imageClassifier.getBufferedImage(file))));
            System.out.println(file.toString() + "  count =  " + sobelImageWhiteCountRatio);

            if (sobelImageWhiteCountRatio >= 0.50) {

                try {
                    Files.copy(file.toPath(), new File("/home/aaron/ImageData/Complex/" + file.getName()).toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Files.copy(file.toPath(), new File("/home/aaron/ImageData/NonComplex/" + file.getName()).toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
}
