package top.imyzt.learning.jackson.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author imyzt
 * @date 2021/07/26
 * @description 参数对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VOA extends BaseVO {

    private String a;
}
