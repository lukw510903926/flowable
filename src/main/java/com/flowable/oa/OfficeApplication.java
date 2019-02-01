package com.flowable.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年1月31日 : 下午5:27:16
 * @description :
 */
@SpringBootApplication
@MapperScan(basePackages = "com.flowable.oa.dao")
public class OfficeApplication extends SpringBootServletInitializer {

    private static Logger logger = LoggerFactory.getLogger(OfficeApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(OfficeApplication.class, args);
        logger.info("office application start successfully----------");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder);
    }
}
