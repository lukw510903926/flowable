package com.flowable.oa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tk.mybatis.mapper.autoconfigure.MapperCacheDisabler;

/**
 * 系统配置
 * @project : tykj-oa
 * @createTime : 2018年2月1日 : 下午12:39:36
 * @author : lukewei
 * @description :
 */
@Configuration
public class ApplicationConfig implements WebMvcConfigurer{

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		LoginInterceptor loginInterceptor = new LoginInterceptor();
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
	}

	@Bean
	@Profile("dev")
	public MapperCacheDisabler mapperCacheDisabler() {
		return new MapperCacheDisabler();
	}
}
