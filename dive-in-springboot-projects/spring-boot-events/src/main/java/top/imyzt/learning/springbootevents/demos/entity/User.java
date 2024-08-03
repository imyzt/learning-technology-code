package top.imyzt.learning.springbootevents.demos.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String username;
    private String address;
}

