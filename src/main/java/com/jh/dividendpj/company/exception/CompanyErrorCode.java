package com.jh.dividendpj.company.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyErrorCode {
    ALREADY_EXIST_COMPANY(400, "이미 데이터베이스에 포함되어있는 회사입니다."),
    NOT_FOUND_NAME(400, "존재하지 않는 회사입니다."),
    NOT_FOUND_TICKER(400, "존재하지 않는 ticker입니다.");

    private int status;
    private String message;
}
