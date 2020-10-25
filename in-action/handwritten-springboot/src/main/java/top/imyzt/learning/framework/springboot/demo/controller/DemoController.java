package top.imyzt.learning.framework.springboot.demo.controller;

import top.imyzt.learning.framework.springboot.annotations.Autowired;
import top.imyzt.learning.framework.springboot.annotations.RequestMapping;
import top.imyzt.learning.framework.springboot.annotations.RequestParam;
import top.imyzt.learning.framework.springboot.annotations.RestController;
import top.imyzt.learning.framework.springboot.demo.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description 描述信息
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("hello")
    public void demo(HttpServletRequest req,
                     @RequestParam(name = "name", defaultValue = "defaultValue") String nametest,
                     HttpServletResponse resp,
                     @RequestParam(name = "hello", required = true) String hello,
                     @RequestParam(name = "world", required = true) String world) throws IOException {

        String result = demoService.hello(nametest) + "  --- hello=[" + hello + "]"  + "  --- world=[" + world + "]";

        resp.getWriter().write(result);

    }
}