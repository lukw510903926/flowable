package com.flowable.oa.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:00
 **/
@Slf4j
@EnableCaching
@SpringBootApplication(scanBasePackages = "com.flowable.oa")
@MapperScan(basePackages = "com.flowable.oa.core.dao")
public class RestFlowApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(RestFlowApiApplication.class, args);
        log.info("flow rest api application start successfully==========");
    }
}
