package top.imyzt.learning.dp.proxy.c_cglib;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description UserDao
 */
public class UserDao{

    public void save() {
        System.out.println("保存用户");
    }

    public void queryUserById(int id) {
        System.out.printf("id=%d, 用户详情\n", id);
    }
}
