package com.example.hecbatch.config.jobconfig;

import com.example.hecbatch.config.listener.StopWatchJobListener;
import com.example.hecbatch.config.stepconfig.StepName;
import com.example.hecbatch.config.stepconfig.bankaccountdelete.BankAccountDeleteProcessor;
import com.example.heccore.bank.model.BankAccountVO;
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

import static com.example.hecbatch.config.stepconfig.Queries.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BankAccountDeleteJobConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DataSource dataSource;
    private final StopWatchJobListener stopWatchJobListener;
    private final static int BANK_ACCOUNT_CHUNK_SIZE = 3;

    @Bean(name = JobName.BANK_ACCOUNT_DELETE_JOB_BEAN)
    public Job bankAccountDeleteJob() {
        return new JobBuilder(JobName.BANK_ACCOUNT_DELETE_JOB, jobRepository)
                .start(bankAccountDeleteStep())
                .listener(stopWatchJobListener)
                .build();
    }

    @Bean(name = StepName.BANK_ACCOUNT_DELETE_STEP)
    @JobScope
    public Step bankAccountDeleteStep() {
        return new StepBuilder(StepName.BANK_ACCOUNT_DELETE_STEP, jobRepository)
                .<BankAccountVO, BankAccountVO>chunk(BANK_ACCOUNT_CHUNK_SIZE, platformTransactionManager)
                .reader(bankAccountDeleteReader())
                .processor(bankAccountDeleteProcessor())
                .writer(bankAccountDeleteWriter())
                .build();
    }

    @Bean(name = StepName.BANK_ACCOUNT_DELETE_ITEM_READER)
    @StepScope
    public JdbcCursorItemReader<BankAccountVO> bankAccountDeleteReader() {
        return new JdbcCursorItemReaderBuilder<BankAccountVO>()
                .name(StepName.BANK_ACCOUNT_DELETE_ITEM_READER)
                .sql(BANK_ACCOUNT_DELETE_READ_QUERY)
                .rowMapper(bankAccountVORowMapper())
                .dataSource(dataSource)
                .saveState(true)
                .build();
    }

    @Bean(name = StepName.BANK_ACCOUNT_DELETE_ITEM_PROCESSOR)
    @StepScope
    public BankAccountDeleteProcessor bankAccountDeleteProcessor() {
        return new BankAccountDeleteProcessor();
    }

    @Bean(name = StepName.BANK_ACCOUNT_DELETE_ITEM_WRITER)
    @StepScope
    public JdbcBatchItemWriter<BankAccountVO> bankAccountDeleteWriter() {
        return new JdbcBatchItemWriterBuilder<BankAccountVO>()
                .dataSource(dataSource)
                .sql(BANK_ACCOUNT_DELETE_WRITE_QUERY)
                .assertUpdates(false)
                .beanMapped()
                .build();
    }

    @Bean
    public RowMapper<BankAccountVO> bankAccountVORowMapper() {
        return ((rs, rowNum) -> {
            BankAccountVO bankAccountVO = new BankAccountVO();
            bankAccountVO.updateForBatchModule(
                    rs.getLong("account_id"),
                    rs.getLong("user_id"),
                    rs.getBoolean("is_deleted")
            );
            return bankAccountVO;
        });
    }

}
