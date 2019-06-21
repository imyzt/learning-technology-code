package top.imyzt.learning.security.browser.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author imyzt
 * @date 2019/06/21
 * @description 社交登录时,显示在前台的信息
 */
@Data
@Accessors(chain = true)
public class SocialUserInfo {

    /**
     * 第三方服务商id
     */
    private String providerId;

    /**
     * 在第三方服务商的唯一标识
     */
    private String providerUserId;

    private String nickname;

    /**
     * 头像
     */
    private String headImg;
}
