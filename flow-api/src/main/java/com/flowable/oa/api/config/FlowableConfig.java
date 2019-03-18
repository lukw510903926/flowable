package com.flowable.oa.api.config;

import com.flowable.oa.core.util.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 流程引擎配置
 *
 * @author yangqi
 * @Description </p>
 * @email 13507615840@163.com
 * @since 2019/3/18 15:54
 **/
@Slf4j
@Configuration
public class FlowableConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {

        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseSchemaUpdate("true");
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("name");
        Map<Object, Object> beans = new HashMap<>();
        processEngineConfiguration.setBeans(beans);
        return processEngineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngineFactory() {

        ProcessEngineFactoryBean processEngineFactory = new ProcessEngineFactoryBean();
        processEngineFactory.setProcessEngineConfiguration(processEngineConfiguration());
        return processEngineFactory;
    }

    @Bean
    public ProcessEngine processEngine() {

        try {
            return processEngineFactory().getObject();
        } catch (Exception e) {
            log.error("流程引擎配置错误 : {}", e);
            System.exit(-1);
            throw new ServiceException("流程引擎配置错误 : {}", e);
        }
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {

        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {

        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {

        return processEngine.getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {

        return processEngine.getManagementService();
    }
}
