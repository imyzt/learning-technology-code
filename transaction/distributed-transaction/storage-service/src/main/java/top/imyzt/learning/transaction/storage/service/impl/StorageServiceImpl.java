package top.imyzt.learning.transaction.storage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.transaction.storage.pojo.entity.Storage;
import top.imyzt.learning.transaction.storage.dao.mapper.StorageMapper;
import top.imyzt.learning.transaction.storage.service.StorageService;
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
@Service
@Slf4j
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(String code, Integer count) {

        log.info("开始减库存");

        Storage storage = this.getStorage(code);

        // 检查库存
        int stock = storage.getCount();
        if (stock <= 0 || stock - count < 0) {
            throw new IllegalArgumentException("库存不足");
        }

        this.updateStock(count, storage, stock);

        log.info("扣减库存成功");
    }

    private void updateStock(Integer count, Storage storage, int stock) {
        lambdaUpdate()
                .set(Storage::getCount, stock - count)
                .eq(Storage::getId, storage.getId())
                .update();
    }

    private Storage getStorage(String code) {
        return Optional.ofNullable(lambdaQuery()
                    .eq(Storage::getCommodityCode, code)
                    .one())
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
    }
}
