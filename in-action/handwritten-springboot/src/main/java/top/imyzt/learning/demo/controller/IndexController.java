package top.imyzt.learning.demo.controller;

import top.imyzt.learning.spring.framework.annotations.RequestMapping;
import top.imyzt.learning.spring.framework.annotations.RequestParam;
import top.imyzt.learning.spring.framework.annotations.RestController;
import top.imyzt.learning.spring.framework.webmvc.servlet.ModalAndView;

import java.util.HashMap;

/**
 * @author imyzt
 * @date 2020/11/01
 * @description 描述信息
 */
@RequestMapping("index")
@RestController
public class IndexController {

    @RequestMapping("first.html")
    public ModalAndView index(@RequestParam(name = "data") String data) {

        HashMap<String, Object> modal = new HashMap<>();
        modal.put("data", data);
        return new ModalAndView("first.html", modal);
    }

}