package top.imyzt.learning.transaction.storage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * storage-service
 * @author imyzt
 */
@SpringBootApplication
@MapperScan("top.imyzt.learning.transaction.storage.dao.mapper")
@EnableDiscoveryClient
public class StorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }

}
