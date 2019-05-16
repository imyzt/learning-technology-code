package top.imyzt.exception.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.exception.enums.ErrorMsg;
import top.imyzt.exception.exception.BusinessException;
import top.imyzt.exception.exception.ParameterException;
import top.imyzt.exception.vo.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * @author imyzt
 * @date 2019/5/8
 * @description IndexController
 */
@RestController
public class IndexController {

    @GetMapping("test_io_ex")
    public Response testIOException(Integer index) {
        if (null != index && index > 0) {
            File file = new File("ffads");
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                System.out.println(listFile);
            }
            return Response.success();
        } else {
            throw new BusinessException(ErrorMsg.FILE_NOT_FOUND);
        }
    }

    @PostMapping("test_npe")
    public Response testNPException(Integer index) {
        System.out.println(index.byteValue());
        return Response.success();
    }

    @PostMapping("test_by_zero")
    public Response testByZero() {
        Response success = Response.success(1 / 0);
        System.out.println(success.hashCode());
        System.out.println(success.toString());
        return success;
    }

    @GetMapping("test_fnf")
    public Response testFileNotFound() {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File("xxx"));
        } catch (FileNotFoundException e) {
            throw new BusinessException(ErrorMsg.FILE_NOT_FOUND, e);
        }
        return null;
    }

    @GetMapping("test_param")
    public Response testParam(Integer age) {

        // 假定str不能为e
        if (null == age) {
            throw new ParameterException("年龄不能为空");
        }
        return Response.success();
    }
}
