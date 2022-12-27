package top.imyzt.learning.office.convert;

import org.apache.poi.hslf.usermodel.HSLFShape;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.hslf.usermodel.HSLFTextShape;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author imyzt
 * @date 2022/12/27
 * @description ppt 转为图片
 */
public class PPTToImageUtil {
    private static final Logger log = LoggerFactory.getLogger(PPTToImageUtil.class);

    public static void main(String[] args) {

        ppt2Png(new File("/tmp/ppt/demo.ppt"));
        pptx2Png(new File("/tmp/ppt/demo.ppt"), "/tmp/ppt/output/pptx");
        pptx2Png(new File("/tmp/ppt/demo.pptm"), "/tmp/ppt/output/pptm");

    }

    /**
     * 将后缀为.pptx的PPT转换为图片
     * @param pptFile PPT的路径
     * @param imgFile 将PPT转换为图片后的路径
     */
    public static List<String> pptx2Png(File pptFile, String imgFile) {
        List<String> list = new ArrayList<>();
        FileInputStream is;
        try {
            is = new FileInputStream(pptFile);

            XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
            is.close();
            // 获取大小
            Dimension pageSize = xmlSlideShow.getPageSize();
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
                BufferedImage img = new BufferedImage(pageSize.width, pageSize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pageSize.width, pageSize.height));

                // 最核心的代码
                slides.get(i).draw(graphics);

                //图片将要存放的路径
                String absolutePath = imgFile + "/" + (i + 1) + ".jpeg";
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
            log.error("PPT转换成图片 发生异常！", e);
        }
        return list;

    }

    /**
     * ppt转为图片列表
     * @param pptFile 在（服务器）本地的ppt文件 比如：123.ppt
     * @return 转换后的图片集合
     */
    public static List<File> ppt2Png(File pptFile) {
        List<File> pngFileList = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        FileInputStream is = null;
        // 将ppt文件转换成每一帧的图片
        HSLFSlideShow ppt = null;

        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            is = new FileInputStream(pptFile);
            ppt = new HSLFSlideShow(is);
            int idx = 1;

            Dimension pageSize = ppt.getPageSize();
            double image_rate = 1.0;
            int imageWidth = (int) Math.floor(image_rate * pageSize.getWidth());
            int imageHeight = (int) Math.floor(image_rate * pageSize.getHeight());

            for (HSLFSlide slide : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, imageWidth, imageHeight));
                graphics.scale(image_rate, image_rate);

                //防止中文乱码
                for (HSLFShape shape : slide.getShapes()) {
                    if (shape instanceof HSLFTextShape) {
                        HSLFTextShape hslfTextShape = (HSLFTextShape) shape;
                        for (HSLFTextParagraph hslfTextParagraph : hslfTextShape) {
                            for (HSLFTextRun hslfTextRun : hslfTextParagraph) {
                                hslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }

                FileOutputStream out = null;
                try {
                    slide.draw(graphics);
                    File pngFile = new File(pptFile.getPath().replace(".ppt", String.format("-%04d.png", idx++)));
                    out = new FileOutputStream(pngFile);
                    ImageIO.write(img, "png", out);
                    pngFileList.add(pngFile);
                } catch (Exception e) {
                    log.error("ppt2Png exception", e);
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }

                        if (graphics != null) {
                            graphics.dispose();
                        }

                        if (img != null) {
                            img.flush();
                        }
                    } catch (IOException e) {
                        log.error("ppt2Png close exception", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("ppt2Png exception", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (ppt != null) {
                    ppt.close();
                }
            } catch (Exception e) {
                log.error("ppt2Png exception", e);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("ppt2Png的时间：{}", endTime - startTime);
        return pngFileList;
    }

    /**
     * ppt2003 文档的转换 后缀名为.ppt
     * @param pptFile ppt文件
     * @param imgFile 图片将要保存的目录（不是文件）
     * @return
     *//*
    public static Boolean doPPT2003toImage(File pptFile,File imgFile,List<String> list) {
        try {
            FileInputStream is = new FileInputStream(pptFile);
            HSLFSlideShow ppt = new HSLFSlideShow(is);
            //及时关闭掉 输入流
            is.close();
            Dimension pgsize = ppt.getPageSize();
            List<HSLFSlide> slide = ppt.getSlides();
            for (int i = 0; i < slide.size(); i++) {
                log.info("第" + i + "页。");
                TextRun[] truns = slide.get(i).getComments().getTextRuns();
                for (int k = 0; k < truns.length; k++) {
                    RichTextRun[] rtruns = truns[k].getRichTextRuns();
                    for (int l = 0; l < rtruns.length; l++) {
                        // 原有的字体索引 和 字体名字
                        int index = rtruns[l].getFontIndex();
                        String name = rtruns[l].getFontName();
                        log.info("原有的字体索引 和 字体名字: "+index+" - "+name);
                        // 重新设置 字体索引 和 字体名称 是为了防止生成的图片乱码问题
                        rtruns[l].setFontIndex(1);
                        rtruns[l].setFontName("宋体");
                    }
                }
                //根据幻灯片大小生成图片
                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.float(0, 0, pgsize.width,pgsize.height));
                slide[i].draw(graphics);
                // 图片的保存位置
                String absolutePath = imgFile.getAbsolutePath()+"/"+ (i + 1) + ".jpeg";
                File jpegFile = new File(absolutePath);
                // 图片路径存放
                list.add((i + 1) + ".jpeg");
                // 如果图片存在，则不再生成
                if (jpegFile.exists()) {
                    continue;
                }
                // 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
                FileOutputStream out = new FileOutputStream(jpegFile);
                ImageIO.write(img, "jpeg", out);
                out.close();
            }
            log.error("PPT转换成图片 成功！");
            return true;
        }
        catch (Exception e) {
            log.error("PPT转换成图片 发生异常！", e);
        }
        return false;
    }*/

}
