package com.jh.dividendpj.dividend.dto;

import lombok.*;

import java.time.LocalDateTime;

public class JoinDividendDto {
    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class Response {
        private LocalDateTime date;
        private String dividend;
    }
}
