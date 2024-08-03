package top.imyzt.learning.springbootevents.demos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.imyzt.learning.springbootevents.demos.entity.User;

/**
 * @author imyzt
 * @date 2024/08/03
 * @description 描述信息
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
