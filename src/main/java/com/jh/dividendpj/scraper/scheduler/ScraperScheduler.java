package com.jh.dividendpj.scraper.scheduler;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.repository.CompanyRepository;
import com.jh.dividendpj.config.CacheKey;
import com.jh.dividendpj.dividend.domain.Dividend;
import com.jh.dividendpj.dividend.repository.DividendRepository;
import com.jh.dividendpj.scraper.YahooScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final YahooScraper yahooScraper;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooScraperSchedule() {
        log.info("스크래핑 스케줄 시작");
        List<Company> companyList = companyRepository.findAll();
        if (!companyList.isEmpty()) {
            for (Company company : companyList) {
                log.info("스크래핑 스케줄 회사 이름 : {}", company.getName());
                List<Dividend> dividendList = yahooScraper.getDividendList(company);
                dividendList.stream()
                        .map(e -> e.toBuilder().company(company).build())
                        .forEach(e -> {
                            boolean isExist = dividendRepository.existsByCompanyAndDate(e.getCompany(), e.getDate());
                            if (!isExist) {
                                dividendRepository.save(e);
                            }
                        });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    log.error("스크래핑 스케줄 스레드 에러 = {}", e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        }
        log.info("스크래핑 스케줄 정상 종료");
    }
}
