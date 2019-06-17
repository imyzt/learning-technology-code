package top.imyzt.learning.security.core.social.qq.api.connet;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import top.imyzt.learning.security.core.social.qq.api.QQ;
import top.imyzt.learning.security.core.social.qq.api.QQUserInfo;

/**
 * @author imyzt
 * @date 2019/6/17
 * @description QQ 适配器, 适配成social统一的格式
 */
public class QQAdapter implements ApiAdapter<QQ> {

    /**
     * 测试服务商是否可用
     */
    @Override
    public boolean test(QQ qq) {
        return true;
    }

    /**
     * 将各个平台中不同的信息封装成统一格式的信息
     */
    @Override
    public void setConnectionValues(QQ qq, ConnectionValues connectionValues) {

        QQUserInfo userInfo = qq.getUserInfo();

        // 显示的用户名
        connectionValues.setDisplayName(userInfo.getNickname());
        // 头像
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        // 用户主页, qq没有对应概念
        //connectionValues.setProfileUrl();
        // 平台唯一标识
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    /**
     * 更新状态, qq没有对应概念
     */
    @Override
    public void updateStatus(QQ qq, String s) {
        // do something
    }
}
