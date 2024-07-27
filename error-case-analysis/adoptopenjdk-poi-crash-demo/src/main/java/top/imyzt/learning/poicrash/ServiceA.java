package top.imyzt.learning.poicrash;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.imyzt.learning.poicrash.pojo.Company;
import top.imyzt.learning.poicrash.service.CompanyService;

import javax.annotation.Resource;

/**
 * @author imyzt
 * @date 2024/07/27
 * @description 描述信息
 */
@Service
public class ServiceA {

    @Resource
    private ServiceB serviceB;
    @Resource
    private CompanyService companyService;

    /**
     * 模拟消费外部的消息, 调用者A
     */
    @Transactional(rollbackFor = Exception.class)
    public void test(String username) {

        // 1. 首先保存自己的数据, 模拟业务操作
        Company company = new Company();
        company.setName("家里蹲");
        company.setAddress("localhost");
        companyService.save(company);

        // 2. 然后调用Spring的代理方法B
        serviceB.doBusiness(username);
    }
}
