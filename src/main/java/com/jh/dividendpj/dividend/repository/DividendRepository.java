package com.jh.dividendpj.dividend.repository;

import com.jh.dividendpj.dividend.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
}
