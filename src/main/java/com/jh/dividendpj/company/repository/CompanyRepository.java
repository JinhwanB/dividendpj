package com.jh.dividendpj.company.repository;

import com.jh.dividendpj.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByTickerAndDelDate(String ticker, LocalDateTime delDate);
}
