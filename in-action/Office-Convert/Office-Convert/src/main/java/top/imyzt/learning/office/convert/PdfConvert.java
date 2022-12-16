package top.imyzt.learning.office.convert;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        String pattern = "yyyy-MM-dd HH:mm:ss";
        System.out.println("开始" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
        PDDocument document = PDDocument.load(new File(filePath));

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        System.out.println("读取完成" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));

//        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            int finalPage = page;
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(finalPage, 100);
            ImageIO.write(bufferedImage, "JPEG", new File(output + "/image" + finalPage + ".jpg"));
            System.out.println(finalPage + "JPEG100 image created." + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
        }

        System.out.println("结束" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
        Thread.sleep(1000000);

        document.close();
    }
}