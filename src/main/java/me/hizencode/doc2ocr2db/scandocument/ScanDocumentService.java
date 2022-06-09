package me.hizencode.doc2ocr2db.scandocument;

import me.hizencode.doc2ocr2db.configs.properties.Doc2ocr2dbProperties;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ScanDocumentService {

    private final ITesseract tesseractInstance;

    private final CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    public ScanDocumentService(Doc2ocr2dbProperties properties, CloudVisionTemplate cloudVisionTemplate) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.tesseractInstance = new Tesseract();
        try {
            this.tesseractInstance.setDatapath(properties.getTesseract().getTrainedDataPath().getFile().getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String doOCRWithGoogleVision(File image) {
        var result = "";
        try {
            result = cloudVisionTemplate.extractTextFromImage(new InputStreamResource(new FileInputStream(image)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String doOCRWithTesseract(File imageToOCR) {

        var result = "";
        try {
            result = tesseractInstance.doOCR(imageToOCR);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return result;
    }
}
