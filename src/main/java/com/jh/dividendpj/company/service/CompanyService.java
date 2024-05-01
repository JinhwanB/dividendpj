package com.jh.dividendpj.company.service;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.exception.CompanyErrorCode;
import com.jh.dividendpj.company.exception.CompanyException;
import com.jh.dividendpj.company.repository.CompanyRepository;
import com.jh.dividendpj.dividend.domain.Dividend;
import com.jh.dividendpj.dividend.service.DividendService;
import com.jh.dividendpj.scraper.YahooScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final YahooScraper yahooScraper;
    private final DividendService dividendService;

    /**
     * 회사 정보 생성
     *
     * @param request 회사의 ticker
     * @return 생성된 회사 정보
     */
    public Company createCompany(CreateCompanyDto.Request request) {
        String ticker = request.getTicker();
        Company company = companyRepository.findByTicker(ticker).orElse(null);
        if (company != null) {
            throw new CompanyException(CompanyErrorCode.ALREADY_EXIST_COMPANY.getMessage());
        }
        company = yahooScraper.getCompany(ticker);
        return companyRepository.save(company);
    }

    /**
     * 회사 삭제
     *
     * @param ticker 삭제할 회사의 ticker
     */
    public void deleteCompany(String ticker) {
        Company company = companyRepository.findByTicker(ticker).orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_TICKER.getMessage()));
        companyRepository.delete(company);
    }

    /**
     * 회사 정보와 배당금 정보 조회
     *
     * @param companyName 조회할 회사 이름
     * @return 조회된 회사 정보와 배당금 정보
     */
    public Company getCompanyInfo(String companyName) {
        Company company = companyRepository.findByName(companyName).orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_NAME.getMessage()));
        List<Dividend> dividendInfo = dividendService.getDividendInfo(company);
        Company withDividend = company.toBuilder()
                .devidendList(dividendInfo)
                .build();
        return companyRepository.save(withDividend);
    }

    /**
     * 회사 조회
     *
     * @param companyName 회사 이름
     * @return 조회된 회사
     */
    @Transactional(readOnly = true)
    public Company getCompany(String companyName) {
        return companyRepository.findByName(companyName).orElseThrow(() -> new CompanyException(CompanyErrorCode.NOT_FOUND_NAME.getMessage()));
    }
}
