package top.imyzt.learning.dp.proxy.a_static;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description App
 */
public class App {

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        // class top.imyzt.learning.dp.proxy.a_static.UserDao
        System.out.println(userDao.getClass());

        UserDaoProxy userDaoProxy = new UserDaoProxy(userDao);
        // class top.imyzt.learning.dp.proxy.a_static.UserDaoProxy
        System.out.println(userDaoProxy.getClass());

        userDaoProxy.save();
    }
}
