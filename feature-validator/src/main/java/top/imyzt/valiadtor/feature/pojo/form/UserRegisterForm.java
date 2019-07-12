package top.imyzt.valiadtor.feature.pojo.form;

import lombok.Data;
import top.imyzt.valiadtor.feature.common.annotation.EnumVerify;
import top.imyzt.valiadtor.feature.common.enums.GenderEnum;

import java.io.Serializable;

/**
 * @author imyzt
 * @date 2019/07/12
 * @description 用户注册表单
 */
@Data
public class UserRegisterForm implements Serializable {

    private String username;

    private String password;

    private String rePass;

    @EnumVerify(message = "请选择正确的性别", enumClass = GenderEnum.class)
    private Integer gander;
}
