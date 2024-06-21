package com.sparta.instahub.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    // AccessToken KEY 값 (이름)
    public static final String ACCESS_HEADER = "Authorization";
    // 사용자 아이디 값의 KEY (이름)
    public static final String ACCESS_USERID = "X-User-Id";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L; // 30분
    private final long REFRESH_TOKEN_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

    @Value("${jwt.secret.key}")
    private static String secretKey;
    private static Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // AccessToken 생성
    public String createAccessToken(String userId) {
        return genereateToken(userId, ACCESS_TOKEN_TIME);
    }

    // RefreshToken 생성
    public String createRefreshToken(String userId) {
        return genereateToken(userId, REFRESH_TOKEN_TIME);
    }

    // userId를 사용하여 Token 생성
    public String genereateToken (String userId, long expiration) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(date.getTime() + expiration))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // AccessToken 검증하기
    public boolean validateAccessToken (String accessToken, String refreshToken, HttpServletResponse response) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("유효하지 않은 AccessToken Signature 입니다.");
        } catch (ExpiredJwtException e) {
            // accessToken 재발급
            log.error("만료된 AccessToken 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 AccessToken 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 AccessToken 입니다.");
        }
        return false;
    }

    // RefreshToken 검증하기
    public boolean validateRefreshToken (String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // Token에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }



}
