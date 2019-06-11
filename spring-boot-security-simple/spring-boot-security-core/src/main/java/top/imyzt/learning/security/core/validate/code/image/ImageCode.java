package top.imyzt.learning.security.core.validate.code.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import top.imyzt.learning.security.core.validate.code.ValidateCode;

import java.awt.image.BufferedImage;

/**
 * @author imyzt
 * @date 2019/6/10
 * @description 图片验证码对象
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageCode extends ValidateCode {

    /**
     * 图片, 根据验证码生成
     */
    private BufferedImage image;


    /**
     * 过期时间点, 传入多少秒后过期, 将对时间进行处理
     * @param expireIn 多久后过期(秒)
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

}
