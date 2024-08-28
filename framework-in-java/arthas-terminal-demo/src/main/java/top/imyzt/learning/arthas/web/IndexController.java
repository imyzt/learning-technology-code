package top.imyzt.learning.arthas.web;

import com.alibaba.arthas.spring.ArthasProperties;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 * @author imyzt
 * @date 2024/08/28
 * @description index
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @Resource
    private ArthasProperties arthasProperties;

    @GetMapping
    public Map<String, String> index(@RequestParam("idx") Integer idx) {
        if (idx < 0) {
            throw new IllegalArgumentException();
        }
        Map<String, String> ret = new HashMap<>(2);
        ret.put("now", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ret.put("appName", arthasProperties.getAppName());
        return ret;
    }
}
