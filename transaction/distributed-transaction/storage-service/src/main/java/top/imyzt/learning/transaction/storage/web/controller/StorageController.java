package top.imyzt.learning.transaction.storage.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imyzt.learning.transaction.storage.service.StorageService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2020/09/26
 * @description 库存控制器
 */
@Slf4j
@RestController
@RequestMapping("storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    /**
     * 扣减库存
     * @param code 商品编号
     * @param count 要扣减的数量
     * @return 无
     */
    @PostMapping("/{code}/{count}")
    public ResponseEntity<Void> deduct(@PathVariable("code") String code, @PathVariable("count") Integer count){
        storageService.deduct(code, count);
        return ResponseEntity.noContent().build();
    }
}