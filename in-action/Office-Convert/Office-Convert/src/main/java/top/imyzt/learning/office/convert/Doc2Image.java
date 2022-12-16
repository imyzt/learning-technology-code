package top.imyzt.learning.office.convert;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Doc2Image {

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {

        String output = "/tmp/Word/output/test.pdf";
        wordToPdf("/tmp/Word/test.docx", "/tmp/Word/output");
        PdfConvert.pdf2Image(output, "/tmp/PdfBox/output/word/");

    }

    public static String wordToPdf(String filePath, String cachePath) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream  fileOutputStream=null;
        try {
            File file = new File(filePath);
            // 读取docx文件
            fileInputStream = new FileInputStream(file);
            XWPFDocument xwpfDocument = new XWPFDocument(fileInputStream);
            PdfOptions pdfOptions = PdfOptions.create();
            // 输出路径
            String outPath = cachePath + File.separator + file.getName();
            fileOutputStream = new FileOutputStream(outPath);
            // 调用转换
            PdfConverter.getInstance().convert(xwpfDocument, fileOutputStream, pdfOptions);
            fileInputStream.close();
            fileOutputStream.close();
            return outPath;
        } catch (IOException e) {
            throw e;
        }
    }
}