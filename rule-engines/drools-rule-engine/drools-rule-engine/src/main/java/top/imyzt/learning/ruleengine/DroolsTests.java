package top.imyzt.learning.ruleengine;


import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author imyzt
 * @date 2025/03/20
 * @description 描述信息
 */
public class DroolsTests {

    public static void main(String[] args) {

        Student s1 = new Student();
        s1.setName("yzt");
        s1.setCity("shenzhen");
        s1.setGender("male");
        s1.setAge(20);
        s1.setScore(60);
        Student s2 = new Student();
        s2.setName("张三");
        s2.setCity("dongguan");
        s2.setGender(null);
        s2.setAge(18);
        s2.setScore(80);

        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);

        Map<String, Object> config = new HashMap<>();
        config.put("minAge", 18);
        config.put("cityList", List.of("shenzhen", "dongguan"));

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        try (KieSession kieSession = kieContainer.newKieSession()) {
            kieSession.setGlobal("config", config);
            kieSession.insert(s1);
            kieSession.insert(s2);
            kieSession.fireAllRules();
            kieSession.dispose();
        }

    }
}
