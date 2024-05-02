package com.jh.dividendpj.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateCompanyDto {
    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class Request {
        @NotBlank(message = "ticker를 입력해주세요.")
        @Size(min = 1, message = "ticker는 최소 1글자 이상이어야 합니다.")
        private String ticker;
    }

    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class Response {
        private String companyName;
        private String ticker;
    }
}
