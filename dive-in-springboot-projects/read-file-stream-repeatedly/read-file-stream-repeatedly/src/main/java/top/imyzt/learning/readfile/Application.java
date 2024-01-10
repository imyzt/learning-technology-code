package top.imyzt.learning.readfile;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author imyzt
 * @date 2024/01/10
 * @description 多次读取MultipartFile的InputStream
 */
@SpringBootApplication
@RequestMapping
@RestController
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        try (
                InputStream is1 = file.getInputStream();
                InputStream is2 = file.getInputStream();
        ) {
            System.out.println(is1);
            System.out.println(is2);
            System.out.println(DigestUtil.md5Hex(is1));
            System.out.println(DigestUtil.md5Hex(is2));
        }
    }

    @PostMapping("/upload/bytes")
    public void uploadBytes(MultipartFile file) throws IOException {

        System.out.println(DigestUtil.md5Hex(file.getBytes()));
        System.out.println(DigestUtil.md5Hex(file.getBytes()));
    }
}
