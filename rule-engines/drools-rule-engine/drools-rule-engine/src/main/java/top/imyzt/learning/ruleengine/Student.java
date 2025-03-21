package top.imyzt.learning.ruleengine;


import lombok.Data;

/**
 * @author imyzt
 * @date 2025/03/20
 * @description Student
 */
@Data
public class Student {

    private String name;

    private Integer age;

    private String gender;

    private String city;

    private Integer score;

    public void addScore(Integer score) {
        System.out.println("name: " + name + " add score: " + score);
        this.score += score;
    }
}
