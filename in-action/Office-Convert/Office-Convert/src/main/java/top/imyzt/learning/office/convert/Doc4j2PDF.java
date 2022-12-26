package top.imyzt.learning.office.convert;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2022/12/25
 * @description 描述信息
 */
public class Doc4j2PDF {

    // 间隔

    // 目录和文件

    public static void main(String[] args) throws Exception {
//        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage
//                .load(new File("/tmp/test.doc"));
//
//        Docx4J.toPDF(wordMLPackage, Files.newOutputStream(new File("/tmp/test-topdf.pdf").toPath()));

        File file = new File("/tmp/doc");
        for (File f : Objects.requireNonNull(file.listFiles())) {
            try {
                doc2pdf(f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void doc2pdf(File tempDocx) throws Exception {
        //加载模板
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(tempDocx);

        //进行数据合并
        //MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
        //数据替换预处理，调用API包
        //在表格替换后调用这个方法
        VariablePrepare.prepare(wordMLPackage);
        //documentPart.variableReplace(variables);
        // 初始化，拿到系统中所有字体
        PhysicalFonts.discoverPhysicalFonts();
        // Set up font mapper (optional)

        setFontMapper(wordMLPackage);
        // FO exporter setup (required)
        // .. the FOSettings object
        FOSettings foSettings = Docx4J.createFOSettings();
        foSettings.setWmlPackage(wordMLPackage);
        OutputStream pdfOutput = new FileOutputStream("/tmp/doc/export/" + tempDocx.getName() + ".pdf");
        Docx4J.toPDF(wordMLPackage, pdfOutput);
        pdfOutput.close();
    }

    private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        //加载字体文件（解决linux环境下无中文字体问题）
        if (PhysicalFonts.get("SimSun") == null) {
            System.out.println("加载本地SimSun字体库");
//        	PhysicalFonts.addPhysicalFonts("SimSun", WordUtils.class.getResource("/fonts/SIMSUN.TTC"));
        }

        //宋体&新宋体

        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("新細明體", PhysicalFonts.get("SimSun"));
        //解决宋体（正文）和宋体（标题）的乱码问题
        PhysicalFonts.put("PMingLiU", PhysicalFonts.get("SimSun"));
        PhysicalFonts.put("新細明體", PhysicalFonts.get("SimSun"));

        //宋体&新宋体
        PhysicalFont simsunFont = PhysicalFonts.get("SimSun");
        fontMapper.put("SimSun", simsunFont);
        //设置字体
        mlPackage.setFontMapper(fontMapper);
    }
}