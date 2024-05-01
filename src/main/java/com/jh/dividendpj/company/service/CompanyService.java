package com.jh.dividendpj.company.service;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.exception.CompanyErrorCode;
import com.jh.dividendpj.company.exception.CompanyException;
import com.jh.dividendpj.company.repository.CompanyRepository;
import com.jh.dividendpj.scraper.YahooScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final YahooScraper yahooScraper;

    public Company createCompany(CreateCompanyDto.Request request) {
        String ticker = request.getTicker();
        Company company = companyRepository.findByTickerAndDelDate(ticker, null).orElse(null);
        if (company != null) {
            throw new CompanyException(CompanyErrorCode.ALREADY_EXIST_COMPANY.getMessage());
        }
        company = yahooScraper.getCompany(ticker);
        return companyRepository.save(company);
    }

    /**
     * 회사 조회
     *
     * @param companyName 회사 이름
     * @return 조회된 회사
     */
    @Transactional(readOnly = true)
    public Company getCompany(String companyName) {
        return companyRepository.findByNameAndDelDate(companyName, null).orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_NAME.getMessage()));
    }
}
