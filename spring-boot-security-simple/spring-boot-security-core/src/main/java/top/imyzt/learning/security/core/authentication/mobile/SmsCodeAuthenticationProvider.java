package top.imyzt.learning.security.core.authentication.mobile;

import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author imyzt
 * @date 2019/6/12
 * @description 短信验证码登录
 */
@Setter
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 用UserDetailsService获取用户信息, 获取到后使用用户信息重新组装给一个authenticationToken信息
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        if (null == userDetails) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        SmsCodeAuthenticationToken smsCodeAuthenticationToken =
                new SmsCodeAuthenticationToken(userDetails, authenticationToken.getAuthorities());

        // 将未认证中的detail(用户ip等信息)复制到已认证的token对象中
        smsCodeAuthenticationToken.setDetails(authenticationToken.getDetails());

        return smsCodeAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
