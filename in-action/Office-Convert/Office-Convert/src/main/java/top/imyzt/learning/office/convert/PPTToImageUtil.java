package top.imyzt.learning.office.convert;

import org.apache.poi.xslf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * pptx 转为图片
 */
public class PPTToImageUtil {
    private static final Logger log = LoggerFactory.getLogger(PPTToImageUtil.class);

    public static void main(String[] args) {
        List list = doPPT2007toImage(new File("/tmp/PPT/test.pptx"), "/tmp/PPT/output/");
        for (Object o : list) {
            System.out.println(o);
        }
    }

    /**
     * 将后缀为.pptx的PPT转换为图片
     * @param pptFile PPT的路径
     * @param imgFile 将PPT转换为图片后的路径
     * @return
     */
    public static List doPPT2007toImage(File pptFile, String imgFile) {
        List<String> list = new ArrayList<>();
        FileInputStream is = null ;
        try {
            is = new FileInputStream(pptFile);

            XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
            is.close();
            // 获取大小
            Dimension pgsize = xmlSlideShow.getPageSize();
            // 获取幻灯片
            List<XSLFSlide> slides = xmlSlideShow.getSlides();

            for (int i = 0 ; i < slides.size() ; i++) {
                // 解决乱码问题
                List<XSLFShape> shapes1 = slides.get(i).getShapes();
                for (XSLFShape shape : shapes1) {

                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape sh = (XSLFTextShape) shape;
                        List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();

                        for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                            List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                            for (XSLFTextRun xslfTextRun : textRuns) {
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }
                //根据幻灯片大小生成图片
                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,pgsize.height));

                // 最核心的代码
                slides.get(i).draw(graphics);

                //图片将要存放的路径
                String absolutePath = imgFile+"/"+ (i + 1) + ".jpeg";
                File jpegFile = new File(absolutePath);
                // 图片路径存放
                list.add((i + 1) + ".jpeg");
                //如果图片存在，则不再生成
                if (jpegFile.exists()) {
                    continue;
                }
                // 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
                FileOutputStream out = new FileOutputStream(jpegFile);

                // 写入到图片中去
                ImageIO.write(img, "jpeg", out);
                out.close();
            }
            log.error("PPT转换成图片 成功！");
            return list;
        } catch (Exception e) {
            log.error("PPT转换成图片 发生异常！{}"+e);
        }
        return list;

    }

}
