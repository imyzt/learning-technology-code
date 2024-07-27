package top.imyzt.learning.poicrash;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@RequestMapping
@RestController
@MapperScan("top.imyzt.learning.poicrash.dao")
public class CrashDemoApplication {

    @Resource
    private ServiceA serviceA;

    public static void main(String[] args) {
        SpringApplication.run(CrashDemoApplication.class, args);
    }


    @PostMapping("/test")
    public void test(String username) {
        serviceA.test(username);
    }

    @GetMapping
    public void download(HttpServletResponse response) throws IOException {

        List<List<String>> head = new ArrayList<>();
        ArrayList<String> head1 = new ArrayList<>();
        head1.add("head1");
        head.add(head1);

        List<List<String>> content = new ArrayList<>();
        ArrayList<String> content1 = new ArrayList<>();
        content1.add("content1");
        content.add(content1);

        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("test", "UTF-8") + ".xlsx");
        EasyExcelFactory.write(response.getOutputStream())
                .head(head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet()
                .doWrite(content);
    }


}
