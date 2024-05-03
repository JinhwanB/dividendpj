package com.jh.dividendpj.member.controller;

import com.jh.dividendpj.auth.TokenProvider;
import com.jh.dividendpj.member.domain.Member;
import com.jh.dividendpj.member.dto.Auth;
import com.jh.dividendpj.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    // 회원가입을 위한 api
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Auth.SignUp request) {
        Member register = memberService.register(request);
        return ResponseEntity.ok(register);
    }

    // 로그인을 위한 api
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Auth.SignIn request) {
        Member authenticate = memberService.authenticate(request);
        String token = tokenProvider.generateToken(authenticate.getUsername(), authenticate.getRoles());
        return ResponseEntity.ok(token);
    }
}
