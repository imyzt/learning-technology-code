package top.imyzt.learning.security.core.validate.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 短信验证码对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidateCode {

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 过期时间点, 传入多少秒后过期, 将对时间进行处理
     * @param expireIn 多久后过期(秒)
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
