package com.flowable.oa.config;

import com.flowable.oa.core.util.file.DiskUploadHelper;
import com.flowable.oa.core.util.file.UploadHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 系统配置
 *
 * @author : lukewei
 * @project : tykj-oa
 * @createTime : 2018年2月1日 : 下午12:39:36
 * @description :
 */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    }

    @Bean
    public UploadHelper uploadHelper() {

        return new DiskUploadHelper();
    }
}
