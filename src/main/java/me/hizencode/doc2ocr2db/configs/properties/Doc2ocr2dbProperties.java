package me.hizencode.doc2ocr2db.configs.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.nio.file.Path;

@Component
@ConfigurationProperties(prefix = "doc2ocr2db")
public class Doc2ocr2dbProperties {

    private Tesseract tesseract;

    public Tesseract getTesseract() {
        return tesseract;
    }

    public void setTesseract(Tesseract tesseract) {
        this.tesseract = tesseract;
    }

    public static class Tesseract {

        private Resource trainedDataPath;

        public Resource getTrainedDataPath() {
            return trainedDataPath;
        }

        public void setTrainedDataPath(Resource trainedDataPath) {
            this.trainedDataPath = trainedDataPath;
        }
    }
}
