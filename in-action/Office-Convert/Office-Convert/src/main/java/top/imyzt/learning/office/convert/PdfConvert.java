package top.imyzt.learning.office.convert;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author imyzt
 * @date 2022/12/15
 * @description PDF -> Image
 */
public class PdfConvert {

    public static void main(String[] args) throws IOException, InterruptedException {

        String pattern = "yyyy-MM-dd HH:mm:ss";
        System.out.println("开始" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
        PDDocument document = PDDocument.load(new File("/tmp/PdfBox/test.pdf"));

        PDFRenderer pdfRenderer = new PDFRenderer(document);

        System.out.println("读取完成" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        int numberOfPages = document.getNumberOfPages();
        CountDownLatch countDownLatch = new CountDownLatch(numberOfPages);

        for (int page = 0; page < numberOfPages; page++) {
            int finalPage = page;
            executorService.execute(() -> {
                try {
                    BufferedImage bufferedImage = pdfRenderer.renderImage(finalPage);
                    boolean jpeg = ImageIO.write(bufferedImage, "JPEG", new File("/tmp/PdfBox/image" + finalPage + ".jpg"));
                    if (finalPage % 20 == 0) {
                        System.out.println(finalPage + ", image created " + jpeg + "," + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));
                    }

                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        countDownLatch.await();

        System.out.println("结束" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern)));

        document.close();

    }
}