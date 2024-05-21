package top.imyzt.learning.spring.startup;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StopWatch;

/**
 * @author imyzt
 * @date 2024/05/21
 * @description 启动类
 */
@Slf4j
@ComponentScan("top.imyzt.learning.spring.startup")
public class Application {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch("Spring启动");
        stopWatch.start();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
