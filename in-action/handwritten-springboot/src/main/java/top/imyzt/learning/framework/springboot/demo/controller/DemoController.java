package top.imyzt.learning.framework.springboot.demo.controller;

import top.imyzt.learning.framework.springboot.annotations.Autowired;
import top.imyzt.learning.framework.springboot.annotations.RequestMapping;
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
    public void demo(HttpServletRequest req, HttpServletResponse resp, String name) throws IOException {

        String result = demoService.hello(name);

        resp.getWriter().write(result);

    }
}