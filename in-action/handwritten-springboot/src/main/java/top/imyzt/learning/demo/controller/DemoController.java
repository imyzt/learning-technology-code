package top.imyzt.learning.demo.controller;

import top.imyzt.learning.demo.service.DemoService;
import top.imyzt.learning.spring.framework.annotations.Autowired;
import top.imyzt.learning.spring.framework.annotations.RequestMapping;
import top.imyzt.learning.spring.framework.annotations.RequestParam;
import top.imyzt.learning.spring.framework.annotations.RestController;
import top.imyzt.learning.spring.framework.webmvc.servlet.ModalAndView;

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
    public ModalAndView demo(HttpServletRequest req,
                             @RequestParam(name = "name", defaultValue = "defaultValue") String nametest,
                             HttpServletResponse resp,
                             @RequestParam(name = "hello", required = true) String hello,
                             @RequestParam(name = "world", required = true) String world) throws IOException {

        String result = demoService.hello(nametest) + "  --- hello=[" + hello + "]"  + "  --- world=[" + world + "]";

        try {
            resp.getWriter().write(result);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("regex*")
    public ModalAndView testRegex(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("content-type","text/html;charset=UTF-8");
        resp.getWriter().write("本次访问地址: " + req.getRequestURI());
        return null;
    }

    @RequestMapping("throwError")
    public ModalAndView throwError() {
        throw new RuntimeException("报错了");
    }
}