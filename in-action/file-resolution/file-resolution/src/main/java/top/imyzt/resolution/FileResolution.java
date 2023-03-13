package top.imyzt.resolution;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import it.sauronsoftware.jave.VideoSize;

import java.io.File;

/**
 * @author imyzt
 * @date 2023/03/13
 * @description 文件视频分辨率检测
 */
public class FileResolution {

    private static final String TMP_VIDEOS = "/tmp/videos";

    public static void main(String[] args) {

        Encoder encoder = new Encoder();

        File dir = new File(TMP_VIDEOS);
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            long start = System.currentTimeMillis();
            MultimediaInfo info;
            try {
                info = encoder.getInfo(file);
            } catch (EncoderException e) {
                System.out.println("file read fail: " + file.getName());
                continue;
            }
            String format = info.getFormat();
            VideoSize videoSize = info.getVideo().getSize();
            System.out.print("file : " + file.getName());
            System.out.print(", format: " + format);
            System.out.print(", height: " + videoSize.getHeight());
            System.out.print(", width: " + videoSize.getWidth());
            System.out.println(", time consuming: [" + (System.currentTimeMillis() - start) + "]");
        }

    }
}