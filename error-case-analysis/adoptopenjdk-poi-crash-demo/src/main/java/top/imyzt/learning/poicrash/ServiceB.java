package top.imyzt.learning.poicrash;


import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.poicrash.pojo.User;
import top.imyzt.learning.poicrash.service.UserService;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author imyzt
 * @date 2024/07/27
 * @description 描述信息
 */
@Service
public class ServiceB {

    @Resource
    private UserService userService;


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void doBusiness(String username) {

        // 3. 这里还有一系列事务操作, 但和这次问题无关, 就不放这里干扰了

        try {
            User user = new User();
            user.setUsername(username);
            user.setAddress("shenzhen");
            // 4. 罪魁祸首的代码, 此处前人为了不干扰主逻辑, catch了异常
            // 为了达到"以为的"异常不干扰外部事务
            userService.saveBatch(Collections.singletonList(user));
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

}
