package top.imyzt.learning.security.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * @author imyzt
 * @date 2019/06/21
 * @description 当完成服务商认证后,跳回本系统时,
 * 根据 {@link org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository#findUserIdsWithConnection } 86 行 if (localUserIds.size() == 0 && connectionSignUp != null)
 * 当connectionSignUp不为空时,会调用connectionSignUp.execute()方法,相当于后台自动创建了一个用户.
 */
@Slf4j
@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        // 此处模拟使用用户在第三方服务商的用户名作为本系统的唯一标识,newUserId
        return connection.getDisplayName();
    }
}
