package com.sparta.instahub.auth.jwt;

import com.sparta.instahub.auth.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


// JwtUtil : - JWT 토큰을 생성하고 검증
@Component
public class JwtUtil {
    private static String secretKey;
    private static long tokenExpiration;
    private static long refreshTokenExpiration;

    private static final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    // 인스턴스화 방지
    private JwtUtil() {
    }

    // JwtConfig를 사용하여 설정 값을 초기화
    public static void init(JwtConfig jwtConfig) {
        secretKey = Base64.getEncoder().encodeToString(jwtConfig.getSecretKey().getBytes());
        tokenExpiration = jwtConfig.getTokenExpiration();
        refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    }

    /**
     * 사용자 이름으로 엑세스 토큰 발급
     *
     * @param username
     * @return
     */
    public static String createAccessToken(String username) {
        return generateToken(username, tokenExpiration);
    }

    /**
     * 사용자 명으로 리프레시 토큰 발급
     *
     * @param username
     * @return
     */
    public static String createRefreshToken(String username) {
        return generateToken(username, refreshTokenExpiration);
    }

    /**
     * 토큰 생성 내부 메서드
     *
     * @param username
     * @param expiration
     * @return
     */
    public static String generateToken(String username, long expiration) {
        return Jwts.builder()
                .setSubject(username) // 토큰 주체
                .setIssuedAt(new Date()) // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 Claims을 추출 페이로드(사용자 정보)
     *
     * @param token JWT 토큰
     * @return 토큰에서 추출한 Claims 객체
     */
    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 유효성 검사: claim의 유저네임값과 userDetail의 이름 비교,
     *
     * @param token
     * @param userDetails
     * @return
     */
    public static boolean validateToken(String token, UserDetails userDetails) {
        // 토큰에서 사용자 이름 추출
        final String username = getUsernameFromToken(token);
        // 토큰 검사(이름 일치, 만료 확인)
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 리프레시토큰 유효성 검사
    public static boolean validateRefreshToken(String token) {
        return validateToken(token);
    }

    // 토큰 유효성 검사
    public static boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 리프레시 토큰으로 토큰 재발급 - 엑세스 토큰 만료시
     *
     * @param refreshToken
     * @return
     */
    public static String refreshToken(String refreshToken) {
        // 리프레시 유효
        if (validateRefreshToken(refreshToken)) {
            String username = getUsernameFromToken(refreshToken);
            return createAccessToken(username);

        } else {
            // 이때 프론트가 적절히 로그인으로 유도
            throw new IllegalArgumentException("Refresh token이 만료 또는 유효하지 않음");
        }
    }

    /**
     * 토큰 만료일 claim 추출
     *
     * @param token JWT 토큰
     * @return 현재 시간
     */
    private static boolean isTokenExpired(String token) {
        final Date expiration = extractClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // 사용자 추출
    public static String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }
}
