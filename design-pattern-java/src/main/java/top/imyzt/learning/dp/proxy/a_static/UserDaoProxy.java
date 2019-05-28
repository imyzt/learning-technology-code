package top.imyzt.learning.dp.proxy.a_static;

import top.imyzt.learning.dp.proxy.utils.PrintUtil;

/**
 * @author imyzt
 * @date 2019/5/28
 * @description 目标对象的静态代理对象,要与目标对象实现一样的接口
 */
public class UserDaoProxy implements IUserDao {

    /**
     * 保存目标对象
     */
    private IUserDao target;

    UserDaoProxy(IUserDao target) {
        this.target = target;
    }

    /**
     * 扩展目标对象的方法, 加上事务
     */
    public void save() {
        PrintUtil.print("开始事务");

        target.save();

        PrintUtil.print("提交事务");
    }

    /**
     * 此方法可见, 静态代理不具备可扩展性. 只需要代理 {@link #save()} 方法时,
     * 因为接口必须继承, 必须将本方法实现
     */
    public void queryUserById(int id) {
        target.queryUserById(id);
    }

}
