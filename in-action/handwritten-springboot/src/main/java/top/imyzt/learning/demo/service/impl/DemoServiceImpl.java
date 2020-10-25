package top.imyzt.learning.demo.service.impl;

import top.imyzt.learning.spring.framework.annotations.Service;
import top.imyzt.learning.demo.service.DemoService;

/**
 * @author imyzt
 * @date 2020/10/24
 * @description 描述信息
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String hello(String name) {
        return "name = [" + name + "]";
    }
}