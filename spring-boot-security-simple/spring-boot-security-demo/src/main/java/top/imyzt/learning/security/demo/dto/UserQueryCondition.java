package top.imyzt.learning.security.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author imyzt
 * @date 2019/6/1
 * @description UserQueryCondition
 */
@Data
public class UserQueryCondition implements Serializable {

    private String username;
    private Integer age;
    private Integer ageTo;

}
