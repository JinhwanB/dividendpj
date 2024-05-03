package com.jh.dividendpj.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private static final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1hour

    @Value("${spring.jwt.secret}")
    private String secret;

    /**
     * 토큰 생성(발급)
     *
     * @param userName
     * @param roles
     * @return
     */
    public String generateToken(String userName, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expireDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secret) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    private Claims parseClaims(String token) {
        
    }
}
