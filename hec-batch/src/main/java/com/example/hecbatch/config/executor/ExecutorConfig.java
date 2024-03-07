package com.example.hecbatch.config.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Bean
    public TaskExecutor taskExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor threadPoolExecutor = new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(processors);
        threadPoolExecutor.setMaxPoolSize(processors * 2);
        threadPoolExecutor.setThreadNamePrefix("multi-thread-");
        threadPoolExecutor.setWaitForTasksToCompleteOnShutdown(false);
        threadPoolExecutor.initialize();
        return threadPoolExecutor;
    }
}
