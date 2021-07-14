package top.imyzt.learning.transaction.account.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.imyzt.learning.transaction.account.service.AccountService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2020/09/26
 * @description 账户控制器
 */
@Slf4j
@RestController
@RequestMapping("account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 扣减余额
     * @param userId 用户id
     * @param money 金额
     */
    @PostMapping("/{userId}/{money}")
    public ResponseEntity<Void> debit(@PathVariable("userId") String userId, @PathVariable("money") Integer money){
        accountService.deduct(userId, money);
        return ResponseEntity.noContent().build();
    }
}