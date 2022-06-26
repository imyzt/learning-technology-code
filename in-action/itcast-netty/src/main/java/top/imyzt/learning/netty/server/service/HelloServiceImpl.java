package top.imyzt.learning.netty.server.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        int i = 1 / 0;
        return "你好, " + msg;
    }
}