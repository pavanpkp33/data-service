package com.sdsu.edu.cms.dataservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan("com.sdsu.edu.cms.dataservice.services")
@EnableAsync
public class DataConfiguration {
    @Bean(name = "notificationExecutor")
    public TaskExecutor specificTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        return executor;
    }
}
