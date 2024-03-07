package com.example.hecbatch.config.jobconfig;

import com.example.hecbatch.config.listener.StopWatchJobListener;
import com.example.hecbatch.config.stepconfig.StepName;
import com.example.hecbatch.config.stepconfig.userdelete.UserDeleteProcessor;
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
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.util.Arrays;

import static com.example.hecbatch.config.stepconfig.Queries.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserDeleteJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;
    private final StopWatchJobListener stopWatchJobListener;
    private final static int USER_CHUNK_SIZE = 3;


    @Bean(name = JobName.USER_DELETE_JOB_BEAN)
    public Job userDeleteJob() {
        return new JobBuilder(JobName.USER_DELETE_JOB, jobRepository)
                .start(userDeleteStep())
                .listener(stopWatchJobListener)
                .build();
    }

    @Bean(name = StepName.USER_DELETE_STEP)
    @JobScope
    public Step userDeleteStep() {
        return new StepBuilder(StepName.USER_DELETE_STEP, jobRepository)
                .<UserVO, UserVO>chunk(USER_CHUNK_SIZE, platformTransactionManager)
                .reader(userDeleteReader())
                .processor(userDeleteProcessor())
                .writer(userDeleteWriter())
                .build();
    }

    @Bean(name = StepName.USER_DELETE_ITEM_READER)
    @StepScope
    public JdbcCursorItemReader<UserVO> userDeleteReader() {
        return new JdbcCursorItemReaderBuilder<UserVO>()
                .name(StepName.USER_DELETE_ITEM_READER)
                .sql(USER_DELETE_READ_QUERY)
                .rowMapper(userVORowMapper())
                .dataSource(dataSource)
                .saveState(true)
                .build();
    }

    @Bean(name = StepName.USER_DELETE_ITEM_PROCESSOR)
    @StepScope
    public UserDeleteProcessor userDeleteProcessor() {
        return new UserDeleteProcessor();
    }

    // 순차적으로 2개의 쿼리를 날려야 하기 때문에 CompositeItemWriter 사용
    @Bean(name = StepName.USER_DELETE_ITEM_WRITER)
    @StepScope
    public CompositeItemWriter<UserVO> userDeleteWriter() {
        CompositeItemWriter<UserVO> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(deleteAccountWriter(), deleteUserWriter()));
        return compositeItemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<UserVO> deleteAccountWriter() {
        return new JdbcBatchItemWriterBuilder<UserVO>()
                .dataSource(dataSource)
                .sql(USER_DELETE_WRITE_FIRST_QUERY)
                .assertUpdates(false)
                .beanMapped()
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<UserVO> deleteUserWriter() {
        return new JdbcBatchItemWriterBuilder<UserVO>()
                .dataSource(dataSource)
                .sql(USER_DELETE_WRITE_SECOND_QUERY)
                .assertUpdates(false)
                .beanMapped()
                .build();
    }




//    @Bean(name = StepName.USER_DELETE_ITEM_WRITER)
//    @StepScope
//    public JdbcBatchItemWriter<UserVO> userDeleteWriter() {
//        return new JdbcBatchItemWriterBuilder<UserVO>()
//                .dataSource(dataSource)
//                .sql(USER_DELETE_WRITE_QUERY)
//                .assertUpdates(false)
//                .beanMapped()
//                .build();
//    }

    @Bean
    public RowMapper<UserVO> userVORowMapper() {
        return ((rs, rowNum) -> {
            UserVO userVO = new UserVO();
            userVO.updateForBatchModule(
                    rs.getLong("user_id"),
                    rs.getBoolean("is_deleted"));
            return userVO;
        });
    }

}
