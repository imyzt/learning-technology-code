package top.imyzt.learning.transaction.account.service;

import top.imyzt.learning.transaction.account.pojo.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
public interface AccountService extends IService<Account> {

    /**
     * 扣余额
     * @param userId 用户标识
     * @param money 金额
     */
    void deduct(String userId, int money);

}
