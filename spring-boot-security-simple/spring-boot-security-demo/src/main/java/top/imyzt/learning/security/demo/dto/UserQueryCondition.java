package top.imyzt.learning.security.demo.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "用户年龄初始值")
    private Integer age;
    @ApiModelProperty(value = "用户年龄终止值")
    private Integer ageTo;

}
