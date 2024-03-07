package com.example.hecbatch.config.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDateTime;

@Configuration
@Slf4j
public class StopWatchJobListener implements JobExecutionListener {

    @Override
    public void afterJob(JobExecution jobExecution){
        LocalDateTime startTime = jobExecution.getStartTime();
        LocalDateTime endTime = jobExecution.getEndTime();

        if(startTime != null && endTime != null){
            Duration duration = Duration.between(startTime, endTime);
            long seconds =duration.getSeconds();
            log.info("============================");
            log.info("총 소요 시간 : "+ seconds + "초");
            log.info("============================");
        }else{
            log.info("Job이 실행되지 않았거나 종료 시간이 기록되지 않았습니다.");
        }
    }
}
