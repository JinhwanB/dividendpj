package com.jh.dividendpj.member.service;

import com.jh.dividendpj.member.domain.Member;
import com.jh.dividendpj.member.dto.Auth;
import com.jh.dividendpj.member.exception.MemberErrorCode;
import com.jh.dividendpj.member.exception.MemberException;
import com.jh.dividendpj.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("등록된 회원이 아닙니다. -> " + username));
    }

    // 회원가입
    // 비밀번호 암호화
    public Member register(Auth.SignUp signUp) {
        boolean isExist = memberRepository.existsByUserName(signUp.getUserName());
        if (isExist) {
            throw new MemberException(MemberErrorCode.ALREADY_EXIST_USERNAME);
        }

        Auth.SignUp encryptionPWD = signUp.toBuilder()
                .password(passwordEncoder.encode(signUp.getPassword()))
                .build();
        return memberRepository.save(encryptionPWD.toEntity());
    }

    // 회원 인증
    public Member authenticate(Auth.SignIn signIn) {
        Member member = memberRepository.findByUserName(signIn.getUserName())
                .orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_USERNAME));
        if (!passwordEncoder.matches(signIn.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.NOT_MATCH_PASSWORD);
        }

        return member;
    }
}
