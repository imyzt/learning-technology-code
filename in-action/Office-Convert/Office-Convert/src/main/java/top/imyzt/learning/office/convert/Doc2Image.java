package top.imyzt.learning.office.convert;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * word to image 乱码问题
 * @author imyzt
 */
public class Doc2Image {

    public static void main(String[] args) throws IOException {

        String output = "/tmp/doc/output/test.pdf";
        wordToPdf("/tmp/doc/demo.docm", output);
        PdfConvert.pdf2Image(output, "/tmp/pdf/output/word/");

    }

    public static String wordToPdf(String filePath, String outPath) throws IOException {
        File file = new File(filePath);
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                FileOutputStream  fileOutputStream = new FileOutputStream(outPath);
        ) {
            // 读取docx文件
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            PdfOptions pdfOptions = PdfOptions.create();

            // 调用转换
            PdfConverter.getInstance().convert(xwpfDocument, fileOutputStream, pdfOptions);
            return outPath;
        }
    }
}