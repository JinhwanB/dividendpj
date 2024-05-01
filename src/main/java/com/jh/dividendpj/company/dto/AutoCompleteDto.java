package com.jh.dividendpj.company.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class AutoCompleteDto {
    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class Request {
        @NotBlank(message = "검색할 단어를 입력하세요.")
        @Size(min = 1)
        private String prefix;
    }
}
