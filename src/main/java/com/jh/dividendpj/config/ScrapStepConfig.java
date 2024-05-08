package com.jh.dividendpj.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ScrapStepConfig {

    @Bean
    public Step scrapStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("scrapStep", jobRepository)
                .chunk(10, transactionManager)
                .build();
    }
}
