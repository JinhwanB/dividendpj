package com.jh.dividendpj.company.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jh.dividendpj.company.dto.AutoCompleteDto;
import com.jh.dividendpj.company.dto.CreateCompanyDto;
import com.jh.dividendpj.company.exception.CompanyErrorCode;
import com.jh.dividendpj.company.service.CompanyService;
import com.jh.dividendpj.scraper.exception.ScraperErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTest {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    String companyName = "Coca-Cola Consolidated, Inc.";
    String ticker = "coke";

    CreateCompanyDto.Request createRequest;
    AutoCompleteDto.Request autoCompleteRequest;

    @BeforeEach
    void before() {
        createRequest = CreateCompanyDto.Request.builder()
                .ticker(ticker)
                .build();
        autoCompleteRequest = AutoCompleteDto.Request.builder()
                .prefix("c")
                .build();
    }

    @Test
    @DisplayName("회사 생성 컨트롤러")
    void createController() throws Exception {
        mockMvc.perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result[0].companyName").value(companyName))
                .andExpect(jsonPath("$.result[0].ticker").value(ticker));
    }

    @Test
    @DisplayName("회사 생성 컨트롤러 실패 - 유효성 검증 실패")
    void failCreateController() throws Exception {
        mockMvc.perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest.toBuilder().ticker("").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(400));
    }

    @Test
    @DisplayName("회사 생성 컨트롤러 실패 - 스크랩 실패")
    void failCreateController2() throws Exception {
        mockMvc.perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest.toBuilder().ticker("가").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(ScraperErrorCode.NOT_FOUND_TICKER.getMessage()));
    }

    @Test
    @DisplayName("회사 삭제 컨트롤러")
    void deleteController() throws Exception {
        companyService.createCompany(createRequest);

        mockMvc.perform(delete("/company/" + ticker))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").doesNotExist());
    }

    @Test
    @DisplayName("회사 삭제 컨트롤러 실패 - 유효성 검증 실패(빈 문자)")
    void failDeleteController() throws Exception {
        mockMvc.perform(delete("/company/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("삭제할 ticker를 입력해주세요."));
    }

    @Test
    @DisplayName("회사 삭제 컨트롤러 실패 - 유효성 검증 실패(공백으로 시작한 문자)")
    void failDeleteController2() throws Exception {
        mockMvc.perform(delete("/company/ "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("ticker의 단어 시작에 공백이 포함되어 있습니다."));
    }

    @Test
    @DisplayName("회사 삭제 컨트롤러 실패 - 저장된 회사 없음")
    void failDeleteController3() throws Exception {
        mockMvc.perform(delete("/company/" + ticker))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(CompanyErrorCode.NOT_FOUND_TICKER.getMessage()));
    }

    @Test
    @DisplayName("자동 완성 기능 컨트롤러")
    void autoCompleteController() throws Exception {
        companyService.createCompany(createRequest);

        mockMvc.perform(get("/company/autocomplete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autoCompleteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result[0].ticker").value(ticker));
    }

    @Test
    @DisplayName("자동 완성 기능 컨트롤러 실패 - 유효성 검증 실패(빈 문자)")
    void autoCompleteController2() throws Exception {
        mockMvc.perform(get("/company/autocomplete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autoCompleteRequest.toBuilder().prefix("").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(400));
    }

    @Test
    @DisplayName("자동 완성 기능 컨트롤러 실패 - 유효성 검증 실패(공백으로 시작하는 문자)")
    void autoCompleteController3() throws Exception {
        mockMvc.perform(get("/company/autocomplete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autoCompleteRequest.toBuilder().prefix(" ").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(400));
    }

    @Test
    @DisplayName("회사 정보와 배당금 정보 조회 컨트롤러")
    void getCompanyWithDividend() throws Exception {
        companyService.createCompany(createRequest);

        mockMvc.perform(get("/finance/dividend/" + companyName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result[0].name").value(companyName))
                .andExpect(jsonPath("$.result[0].dividendDtoList").exists());
    }

    @Test
    @DisplayName("회사 정보와 배당금 정보 조회 컨트롤러 실패 - 유효성 검증 실패(빈 문자)")
    void failGetCompanyWithDividend() throws Exception {
        mockMvc.perform(get("/finance/dividend/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("조회할 companyName을 입력해주세요."));
    }

    @Test
    @DisplayName("회사 정보와 배당금 정보 조회 컨트롤러 실패 - 유효성 검증 실패(공백 문자로 시작)")
    void failGetCompanyWithDividend2() throws Exception {
        mockMvc.perform(get("/finance/dividend/ "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("companyName의 단어 시작에 공백이 포함되어 있습니다."));
    }

    @Test
    @DisplayName("회사 정보와 배당금 정보 조회 컨트롤러 실패 - 저장된 회사 정보 없음")
    void failGetCompanyWithDividend3() throws Exception {
        mockMvc.perform(get("/finance/dividend/" + companyName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(CompanyErrorCode.NOT_FOUND_NAME.getMessage()));
    }

    @Test
    @DisplayName("현재 관리하고 있는 모든 회사 리스트 조회 컨트롤러")
    void getAllCompany() throws Exception {
        companyService.createCompany(createRequest);

        mockMvc.perform(get("/company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result[0].content").exists());
    }

    @Test
    @DisplayName("현재 관리하고 있는 모든 회사 리스트 조회 컨트롤러 - 결과 없음")
    void getAllCompany2() throws Exception {
        mockMvc.perform(get("/company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.result[0].content").isEmpty());
    }
}