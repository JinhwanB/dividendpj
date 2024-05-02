package com.jh.dividendpj.company.controller;

import com.jh.dividendpj.company.domain.Company;
import com.jh.dividendpj.company.dto.CompanyDto;
import com.jh.dividendpj.company.dto.CompanyWithDividendDto;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.service.CompanyService;
import com.jh.dividendpj.config.GlobalApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompanyController {
    private final CompanyService companyService;

    // 회사 생성
    @PostMapping("/company")
    public ResponseEntity<GlobalApiResponse> create(@Valid @RequestBody CreateCompanyDto.Request request) {
        log.info("입력받은 ticker={}", request.getTicker());

        Company company = companyService.createCompany(request);
        List<CreateCompanyDto.Response> list = new ArrayList<>(List.of(company.toCreateResponseDto()));
        return ResponseEntity.ok(GlobalApiResponse.toGlobalApiResponse(list));
    }

    // 회사 삭제
    @DeleteMapping(value = {"/company/", "/company/{ticker}"})
    public ResponseEntity<GlobalApiResponse> delete(@PathVariable Optional<String> ticker) {
        log.info("삭제할 ticker={}", ticker);

        String notEmptyTicker = ticker.orElseThrow(() -> new IllegalArgumentException("삭제할 ticker를 입력해주세요."));

        if (notEmptyTicker.startsWith(" ")) {
            throw new IllegalArgumentException("ticker의 단어 시작에 공백이 포함되어 있습니다.");
        }

        companyService.deleteCompany(notEmptyTicker);
        GlobalApiResponse response = GlobalApiResponse.builder()
                .message("성공")
                .status(200)
                .build();
        return ResponseEntity.ok(response);
    }

    // 자동완성 기능을 위한 API
    @GetMapping("/company/autocomplete")
    public ResponseEntity<GlobalApiResponse> autoComplete(@Valid @RequestBody CompanyDto.Request request) {
        log.info("검색한 단어={}", request.getPrefix());

        List<Company> autoComplete = companyService.getAutoComplete(request);
        List<CompanyDto.Response> list = autoComplete.stream().map(Company::toCompanyResponseDto).toList();
        return ResponseEntity.ok(GlobalApiResponse.toGlobalApiResponse(list));
    }

    // 회사 정보와 배당금 정보 조회 컨트롤러
    @GetMapping(value = {"/finance/dividend/", "/finance/dividend/{companyName}"})
    public ResponseEntity<GlobalApiResponse> getCompanyWithDividend(@PathVariable Optional<String> companyName) {
        log.info("조회할 회사명={}", companyName);

        String notEmptyCompanyName = companyName.orElseThrow(() -> new IllegalArgumentException("조회할 companyName을 입력해주세요."));

        if (notEmptyCompanyName.startsWith(" ")) {
            throw new IllegalArgumentException("companyName의 단어 시작에 공백이 포함되어 있습니다.");
        }

        Company companyInfo = companyService.getCompanyInfo(notEmptyCompanyName);
        List<CompanyWithDividendDto.Response> list = new ArrayList<>(List.of(companyInfo.toCompanyWithDividendDto()));
        return ResponseEntity.ok(GlobalApiResponse.toGlobalApiResponse(list));
    }

    // 현재 관리하고 있는 모든 회사 리스트 조회
    @GetMapping("/company")
    public ResponseEntity<GlobalApiResponse> getAllCompany(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Company> allCompany = companyService.getAllCompany(pageable);
        List<CompanyDto.Response> pageList = allCompany.stream().map(Company::toCompanyResponseDto).toList();
        Page<CompanyDto.Response> allPageList = new PageImpl<>(pageList, pageable, pageList.size());
        List<Page<CompanyDto.Response>> list = new ArrayList<>(List.of(allPageList));
        return ResponseEntity.ok(GlobalApiResponse.toGlobalApiResponse(list));
    }
}
