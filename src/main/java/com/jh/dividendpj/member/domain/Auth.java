package com.jh.dividendpj.member.domain;

import lombok.*;

import java.util.List;

public class Auth {
    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class SignIn {
        private String userName;
        private String password;
    }

    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class SignUp {
        private String userName;
        private String password;
        private List<String> roles;

        public Member toEntity() {
            return Member.builder()
                    .userName(userName)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
