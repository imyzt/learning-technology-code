package top.imyzt.flowable.props;


import lombok.Data;

/**
 * @author imyzt
 * @date 2025/05/24
 * @description 描述信息
 */
@Data
public class TimerProps {

    private String waitType;
    private String unit;
    private String duration;
    private String timeDate;
}
