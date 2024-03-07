package com.example.hecbatch.config;

import com.example.hecbatch.config.jobconfig.JobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class BatchScheduleConfiguration {

    private final JobLauncher jobLauncher;
    private final Job deleteJob;


    public BatchScheduleConfiguration(JobLauncher jobLauncher,
                                      @Qualifier(JobName.DELETE_JOB_BEAN) Job deleteJob){
        this.jobLauncher = jobLauncher;
        this.deleteJob = deleteJob;
    }
    /**
     * @1분마다 "0 * * * * *"
     * @자정마다 "0 0 0 * * *"
     * @10초마다 "0/10 * * * * *"
     */
    @Scheduled(cron = "0/10 * * * * *")
    public void runDeleteBatch(){
        log.info("=====================딜리트 배치 실행=====================");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("version", "1.0")
                .addString("usage", "Delete")
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        try{
            jobLauncher.run(deleteJob, jobParameters);
        }catch (JobExecutionAlreadyRunningException | JobRestartException |
                JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

}
