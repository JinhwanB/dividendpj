package com.jh.dividendpj.company.repository;

import com.jh.dividendpj.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByTicker(String ticker);

    List<Company> findAllByTicker(String ticker);

    Optional<Company> findByName(String name);
}
