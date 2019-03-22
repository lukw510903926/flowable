package com.flowable.oa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/22 17:53
 **/
@EnableCaching
@SpringBootApplication
@MapperScan(basePackages = "com.flowable.oa.core.dao")
@ServletComponentScan("com.flowable.oa")
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
