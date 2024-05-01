package com.jh.dividendpj.company.controller;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.service.CompanyService;
import com.jh.dividendpj.global.GlobalApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompanyController {
    private final CompanyService companyService;

    // 회사 생성
    @PostMapping
    public ResponseEntity<GlobalApiResponse> test(@Valid @RequestBody CreateCompanyDto.Request request) {
        log.info("입력받은 ticker={}", request.getTicker());

        Company company = companyService.createCompany(request);
        List<CreateCompanyDto.Response> list = new ArrayList<>(List.of(company.toCreateResponseDto()));
        return ResponseEntity.ok(GlobalApiResponse.toGlobalApiResponse(list));
    }

    // 회사 삭제
    @DeleteMapping("/{ticker}")
    public ResponseEntity<GlobalApiResponse> testDelete(@PathVariable @NotBlank(message = "삭제할 ticker를 입력해주세요.") @Size(min = 1, message = "ticker는 최소 1글자 이상입니다.") String ticker) {
        companyService.deleteCompany(ticker);
        GlobalApiResponse response = GlobalApiResponse.builder()
                .message("성공")
                .status(200)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company")
    public Company testGet(@RequestParam String companyName) {
        return companyService.getCompany(companyName);
    }
}
