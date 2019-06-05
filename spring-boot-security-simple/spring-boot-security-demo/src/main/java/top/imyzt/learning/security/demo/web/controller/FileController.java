package top.imyzt.learning.security.demo.web.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.imyzt.learning.security.demo.dto.FileInfo;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author imyzt
 * @date 2019/6/4
 * @description FileController
 */
@RestController
@RequestMapping("file")
@Slf4j
@Api(value = "文件服务")
public class FileController {

    private static final String FOLDER = "D:\\dev\\imyzt\\learning-technology-code\\spring-boot-security-simple\\spring-boot-security-demo\\src\\main\\java\\top\\imyzt\\learning\\security\\demo\\web\\controller";


    @PostMapping
    public FileInfo upload(MultipartFile file) throws IOException {

        log.info("file originalFilename: {}, name: {}, size: {}, ", file.getOriginalFilename(), file.getName(), file.getSize());


        File saveFile = new File(FOLDER, System.currentTimeMillis() + ".txt");
        file.transferTo(saveFile);
        return new FileInfo(saveFile.getAbsolutePath());
    }

    @GetMapping("{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {

        try (FileInputStream fis = new FileInputStream(new File(FOLDER, id + ".txt"));
             OutputStream os = response.getOutputStream()) {

            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=test.txt");

            IOUtils.copy(fis, os);
            os.flush();
        }
    }
}
