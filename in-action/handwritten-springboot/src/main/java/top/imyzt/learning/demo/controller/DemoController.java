package top.imyzt.learning.demo.controller;

import cn.hutool.core.lang.Pair;
import top.imyzt.learning.demo.service.DemoService;
import top.imyzt.learning.spring.framework.annotations.*;
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
    @ResponseBody
    public String demo(HttpServletRequest req,
                             @RequestParam(name = "name", defaultValue = "defaultValue") String nametest,
                             HttpServletResponse resp,
                             @RequestParam(name = "hello", required = true) String hello,
                             @RequestParam(name = "world", required = true) String world) {

        return demoService.hello(nametest) + "  --- hello=[" + hello + "]"  + "  --- world=[" + world + "]";
    }

    @RequestMapping("json")
    @ResponseBody
    public Pair<String, String> demo(@RequestParam(name = "value", defaultValue = "defaultValue") String value) {

        return new Pair<>("key", value);
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