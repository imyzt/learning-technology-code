package top.imyzt.learning.office.convert;

import cn.hutool.core.io.FileUtil;
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

    public static void main(String[] args) throws IOException {

        pdf2Image("/tmp/PdfBox/test.pdf", "/tmp/PdfBox/output");

    }

    public static void pdf2Image(String filePath, String output) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 100);
            ImageIO.write(bufferedImage, "JPEG", FileUtil.touch(output + "/image" + page + ".jpg"));
        }

        document.close();
    }
}