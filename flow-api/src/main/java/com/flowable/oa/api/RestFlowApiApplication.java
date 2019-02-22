package com.flowable.oa.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/2/22 22:00
 **/
@SpringBootApplication(scanBasePackages = "com.flowable.oa")
@MapperScan(basePackages = "com.flowable.oa.core.dao")
public class RestFlowApiApplication {

    private static Logger logger = LoggerFactory.getLogger(RestFlowApiApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(RestFlowApiApplication.class, args);
        logger.info("flow rest api application start successfully==========");
    }
}
