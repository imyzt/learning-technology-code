package top.imyzt.learning.transaction.account.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.transaction.account.pojo.entity.Account;
import top.imyzt.learning.transaction.account.dao.mapper.AccountMapper;
import top.imyzt.learning.transaction.account.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String userId, int money) {

        log.info("开始扣余额");

        Account account = this.getAccount(userId);

        Integer balance = account.getMoney();
        if (balance <= 0 || balance - money < 0) {
            throw new IllegalArgumentException("账户余额不足");
        }

        this.updateBalance(userId, money, balance);

        log.info("余额扣除成功");
        
    }

    private void updateBalance(String userId, int money, Integer balance) {
        lambdaUpdate()
                .set(Account::getMoney, balance - money)
                .eq(Account::getUserId, userId)
                .update();
    }

    private Account getAccount(String userId) {
        return (Account) Optional.ofNullable(lambdaQuery().eq(Account::getUserId, userId).one())
                    .orElseThrow(() -> new IllegalArgumentException("账户不存在"));
    }
}
