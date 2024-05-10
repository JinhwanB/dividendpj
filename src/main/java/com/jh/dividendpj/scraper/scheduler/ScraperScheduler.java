package com.jh.dividendpj.scraper.scheduler;

import com.jh.dividendpj.config.CacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class ScraperScheduler {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @Value("${job.scrap.job-name}")
    private String jobName;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooScraperSchedule() {
        log.info("스케줄 시작");
        String time = LocalDateTime.now().toString();
        try{
            Job scrapJob = jobRegistry.getJob(jobName);
            JobParametersBuilder jobParam = new JobParametersBuilder().addString("time", time);
            jobLauncher.run(scrapJob, jobParam.toJobParameters());
        }catch (NoSuchJobException | JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                JobParametersInvalidException | JobRestartException e){
            log.error("스크랩 스케줄 도중 예외 발생!! -> {}", e.getMessage());
            throw new RuntimeException(e);
        }
//        log.info("스크래핑 스케줄 시작");
//        // todo: 회사가 많아질 경우 시간이 굉장히 오래 걸릴 가능성 있음! spring batch에 대해 알아보기!
//        List<Company> companyList = companyRepository.findAll();
//        if (!companyList.isEmpty()) {
//            for (Company company : companyList) {
//                log.info("스크래핑 스케줄 회사 이름 : {}", company.getName());
//                List<Dividend> dividendList = yahooScraper.getDividendList(company);
//                dividendList.stream()
//                        .map(e -> e.toBuilder().company(company).build())
//                        .forEach(e -> {
//                            boolean isExist = dividendRepository.existsByCompanyAndDate(e.getCompany(), e.getDate());
//                            if (!isExist) {
//                                dividendRepository.save(e);
//                            }
//                        });
//
//                try {
//                    Thread.sleep(3000); // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
//                } catch (InterruptedException e) {
//                    log.error("스크래핑 스케줄 스레드 에러 = {}", e.getMessage());
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//        log.info("스크래핑 스케줄 정상 종료");
    }
}
