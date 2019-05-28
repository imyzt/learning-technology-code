package top.imyzt.learning.dp.proxy.b_dynamic;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description UserDao
 */
public class UserDao implements IUserDao {

    @Override
    public void save() {
        System.out.println("保存用户");
    }

    @Override
    public void queryUserById(int id) {
        System.out.printf("id=%d, 用户详情", id);
    }
}
