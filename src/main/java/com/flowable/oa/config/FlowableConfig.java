package com.flowable.oa.config;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import com.flowable.oa.util.exception.ServiceException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.spring.ProcessEngineFactoryBean;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 流程引擎配置
 * @project : tykj-oa
 * @createTime : 2018年2月1日 : 下午12:39:48
 * @author : lukewei
 * @description :
 */
@Component
public class FlowableConfig {
	
	private Logger logger = LoggerFactory.getLogger(FlowableConfig.class);

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
        Map<Object, Object> beans = new HashMap<Object, Object>();
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
            logger.error("流程引擎配置错误 : {}", e);
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
