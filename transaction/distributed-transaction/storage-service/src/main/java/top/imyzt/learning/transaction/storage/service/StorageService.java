package top.imyzt.learning.transaction.storage.service;

import top.imyzt.learning.transaction.storage.pojo.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author imyzt
 * @date 2020-09-26
 */
public interface StorageService extends IService<Storage> {

    /**
     * 减库存
     * @param code 商品code
     * @param count 购买数量
     */
    void deduct(String code, Integer count);

}
