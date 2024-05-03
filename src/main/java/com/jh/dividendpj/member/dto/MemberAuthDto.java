package com.jh.dividendpj.member.dto;

import com.jh.dividendpj.member.domain.Member;
import lombok.*;

import java.util.List;

public class MemberAuthDto {
    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class SignIn {
        private String username;
        private String password;
    }

    @Getter
    @Setter // 테스트용
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder(toBuilder = true)
    @ToString
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public Member toEntity() {
            return Member.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
