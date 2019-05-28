package top.imyzt.learning.dp.proxy.a_static;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description 模拟用户操作DAO接口
 */
public interface IUserDao {

    /**
     * 保存用户
     */
    void save();

    /**
     * 根据id查询用户
     * @param id 用户id
     */
    void queryUserById(int id);
}
