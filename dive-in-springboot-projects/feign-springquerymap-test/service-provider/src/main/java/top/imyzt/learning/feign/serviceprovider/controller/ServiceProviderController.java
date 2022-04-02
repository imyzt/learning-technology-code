package top.imyzt.learning.feign.serviceprovider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author imyzt
 * @date 2021/12/07
 * @description 服务提供方
 */
@RestController
@RequestMapping("/service-provider")
public class ServiceProviderController {

    @GetMapping
    public String queryTest(@RequestParam TestBean testBean) {
        return testBean.getA() + "_" + testBean.getB();
    }

    public static class TestBean {

        private String a;
        private String b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}
