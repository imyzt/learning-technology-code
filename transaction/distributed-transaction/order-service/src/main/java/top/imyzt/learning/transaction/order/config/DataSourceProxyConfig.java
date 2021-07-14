package top.imyzt.learning.transaction.order.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author imyzt
 * @date 2020/09/26
 * @description Seata代理数据源
 */
@Configuration
public class DataSourceProxyConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        // mybatis-plus, 需要使用特殊的SqlSessionFactoryBean
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        // 代理数据源
        factoryBean.setDataSource(new DataSourceProxy(dataSource));
        // 生成SqlSessionFactory
        return factoryBean.getObject();
    }
}