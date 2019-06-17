package top.imyzt.learning.security.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2019/6/5
 * @description 自定义用户信息服务类
 */
@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService, SocialUserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("表单登录用户名: {}", username);

        return buildUser(username);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        log.info("社交登录用户id: {}", userId);

        return buildUser(userId);
    }

    private SocialUser buildUser(String userId) {
        String password = passwordEncoder.encode("123456");
        log.info("数据库的密码是: {}", password);
        return new SocialUser(userId, password,
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
