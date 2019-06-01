package top.imyzt.learning.security.demo.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * @author imyzt
 * @date 2019/6/1
 * @description User
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {

    public interface UserSimpleView {}
    public interface UserDetailView extends UserSimpleView {}

    private String id;

    @JsonView(UserSimpleView.class)
    private String username;

    @JsonView(UserDetailView.class)
    @NotBlank(message = "密码不能为空")
    private String password;

    private Date birthday;
}
