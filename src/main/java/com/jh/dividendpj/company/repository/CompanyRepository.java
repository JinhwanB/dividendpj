package com.jh.dividendpj.company.repository;

import com.jh.dividendpj.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByTickerAndDelDate(String ticker, LocalDateTime delDate);

    Optional<Company> findByNameAndDelDate(String name, LocalDateTime delDate);
}
