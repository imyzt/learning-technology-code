package top.imyzt.learning.security.demo.service;

import org.springframework.stereotype.Service;

/**
 * @author imyzt
 * @date 2019/6/3
 * @description HelloServiceImpl
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void greeting(String name) {
        System.out.println(name);
    }
}
