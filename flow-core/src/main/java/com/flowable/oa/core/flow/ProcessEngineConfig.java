package com.flowable.oa.core.flow;

import com.flowable.oa.core.util.exception.ServiceException;
import javax.annotation.Resource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author : yangqi
 * @email : lukewei@mockuai.com
 * @description : 流程引擎配置
 * @since : 2020/2/27 10:28 上午
 */
@Slf4j
@Configuration
public class ProcessEngineConfig {

    @Resource
    private DataSource dataSource;

    @Resource
    private PlatformTransactionManager transactionManager;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {

        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        processEngineConfiguration.setTransactionManager(transactionManager);
        processEngineConfiguration.setDatabaseSchemaUpdate(SpringProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
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
            log.error("流程引擎配置错误 : ", e);
            System.exit(-1);
            throw new ServiceException("流程引擎配置错误", e);
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
