package me.hizencode.doc2ocr2db.scandocument;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
public class ScanDocumentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ScanDocumentController.class);

    private final ScanDocumentService scanDocumentService;

    @Autowired
    public ScanDocumentController(ScanDocumentService scanDocumentService) {
        this.scanDocumentService = scanDocumentService;
    }

    @GetMapping(value = {"/", "/scan-document"})
    public String showScanDocumentPage() {
        return "scanDocument/scanDocument";
    }

    @PostMapping(value = {"/scan-document"})
    public void scanDocumentImageAndReturnTextFile(@RequestParam("documentToScan") MultipartFile file, HttpServletResponse response) {
        LOGGER.info("Uploading image for OCR");

        try {
            var tempFileName = UUID.randomUUID() + file.getOriginalFilename();
            var tempFile = new File( "D:\\Projects\\Personal\\Doc2ocr2dbProject\\Temp\\" + tempFileName);

            try (var os = new FileOutputStream(tempFile)) {
                os.write(file.getBytes());
            }

            LOGGER.info("File name: " + tempFile.getName());
            LOGGER.info("File size: " + tempFile.length() / 1000000D + " MB");

            var result = scanDocumentService.doOCRWithGoogleVision(tempFile);

            var resultStream = new ByteArrayInputStream(result.getBytes());
            IOUtils.copy(resultStream, response.getOutputStream());
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment; filename=\"result.txt\"");
            response.flushBuffer();

            if (tempFile.delete()) {
                LOGGER.info("File deleted: " + tempFileName);
            } else {
                LOGGER.warn("Failed to delete file: " + tempFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
