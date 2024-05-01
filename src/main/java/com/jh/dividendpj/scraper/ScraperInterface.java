package com.jh.dividendpj.scraper;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.dividend.domain.Dividend;

import java.util.List;

public interface ScraperInterface {
    Company getCompany(String ticker);

    List<Dividend> getDividendList(Company company);
}
