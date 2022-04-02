package top.imyzt.learning.spel.speldemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author imyzt
 * @date 2022/04/02
 * @description 描述信息
 */
@RestController
public class AopController {

    @GetMapping
    @DemoSpel(value = "'LOTTERY:HELP:' + #lotteryId + ':' + #student.name.concat(#student.id)")
    public String method(Student student, Integer lotteryId) {

        return student.toString();
    }

    public static class Student {
        private String id;
        private String name;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
