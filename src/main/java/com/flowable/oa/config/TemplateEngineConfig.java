package com.flowable.oa.config;

import java.util.LinkedHashSet;
import java.util.Set;

import com.flowable.oa.util.thymeleaf.HasPermissionDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-07 14:19
 **/
@Configuration
public class TemplateEngineConfig {


	@Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
    	
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        Set<IDialect> additionalDialects = new LinkedHashSet<>();
        additionalDialects.add(new HasPermissionDialect());
        templateEngine.setAdditionalDialects(additionalDialects);
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

}
