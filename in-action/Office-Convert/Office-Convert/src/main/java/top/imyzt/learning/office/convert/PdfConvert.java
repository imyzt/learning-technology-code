package top.imyzt.learning.office.convert;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2022/12/15
 * @description PDF -> Image
 */
public class PdfConvert {

    public static void main(String[] args) throws IOException, InterruptedException {

        pdf2Image("/tmp/PdfBox/test.pdf", "/tmp/PdfBox/output");

    }

    public static void pdf2Image(String filePath, String output) throws IOException, InterruptedException {
        PDDocument document = PDDocument.load(new File(filePath));

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            int finalPage = page;
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(finalPage, 100);
            ImageIO.write(bufferedImage, "JPEG", new File(output + "/image" + finalPage + ".jpg"));
        }

        Thread.sleep(1000000);

        document.close();
    }
}