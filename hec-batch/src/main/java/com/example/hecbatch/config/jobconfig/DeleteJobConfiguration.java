package com.example.hecbatch.config.jobconfig;

import com.example.hecbatch.config.listener.StopWatchJobListener;
import com.example.hecbatch.config.stepconfig.StepName;
import com.example.hecbatch.config.stepconfig.delete.DeleteProcessor;
import com.example.heccore.user.model.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import static com.example.hecbatch.config.stepconfig.Queries.DELETE_READ_QUERY;
import static com.example.hecbatch.config.stepconfig.Queries.DELETE_WRITE_QUERY;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeleteJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;
    private final StopWatchJobListener stopWatchJobListener;
    private final int CHUNK_SIZE = 3;


    @Bean(name = JobName.DELETE_JOB_BEAN)
    public Job deleteJob() {
        return new JobBuilder(JobName.DELETE_JOB, jobRepository)
                .start(deleteStep())
                .listener(stopWatchJobListener)
                .build();
    }

    @Bean(name = StepName.DELETE_STEP)
    @JobScope
    public Step deleteStep() {
        return new StepBuilder(StepName.DELETE_STEP, jobRepository)
                .<UserVO, UserVO>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(deleteReader())
                .processor(deleteProcessor())
                .writer(deleteWriter())
                .build();
    }

    @Bean(name = StepName.DELETE_ITEM_READER)
    @StepScope
    public JdbcCursorItemReader<UserVO> deleteReader() {
        return new JdbcCursorItemReaderBuilder<UserVO>()
                .name(StepName.DELETE_ITEM_READER)
                .sql(DELETE_READ_QUERY)
                .rowMapper(userVORowMapper())
                .dataSource(dataSource)
                .saveState(true)
                .build();
    }

    @Bean(name = StepName.DELETE_ITEM_PROCESSOR)
    @StepScope
    public DeleteProcessor deleteProcessor() {
        return new DeleteProcessor();
    }

    @Bean(name = StepName.DELETE_ITEM_WRITER)
    @StepScope
    public JdbcBatchItemWriter<UserVO> deleteWriter() {
        return new JdbcBatchItemWriterBuilder<UserVO>()
                .dataSource(dataSource)
                .sql(DELETE_WRITE_QUERY)
                .assertUpdates(false)
                .beanMapped()
                .build();
    }

    @Bean
    public RowMapper<UserVO> userVORowMapper() {
        return ((rs, rowNum) -> {
            UserVO userVO = new UserVO();
            userVO.setUserId(rs.getLong("user_id"));
            userVO.setName(rs.getString("name"));
            userVO.setDeleted(rs.getBoolean("is_deleted"));
            return userVO;
        });
    }

}
