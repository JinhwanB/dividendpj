package com.jh.dividendpj.company.controller;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companys")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/company")
    public ResponseEntity<CreateCompanyDto.Response> test(@Valid @RequestBody CreateCompanyDto.Request request) {
        log.info("입력받은 ticker={}", request.getTicker());

        Company company = companyService.createCompany(request);
        return ResponseEntity.ok(company.toCreateResponseDto());
    }

    @GetMapping("/company")
    public Company testGet(@RequestParam String companyName) {
        return companyService.getCompany(companyName);
    }

    @DeleteMapping("/company")
    public String testDelete(@RequestParam String ticker) {
        companyService.deleteCompany(ticker);
        return "성공";
    }
}
