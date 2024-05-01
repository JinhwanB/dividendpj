package com.jh.dividendpj.dividend.service;

import com.jh.dividendpj.dividend.repository.DividendRepository;
import com.jh.dividendpj.scraper.YahooScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DividendService {
    private final DividendRepository dividendRepository;
    private final YahooScraper yahooScraper;
}
