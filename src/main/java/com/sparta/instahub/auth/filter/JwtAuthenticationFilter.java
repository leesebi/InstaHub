package com.sparta.instahub.auth.filter;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserStatus;
import com.sparta.instahub.auth.jwt.JwtUtil;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.auth.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Jwt 인증필터: 모든 HTTP가 거침
 * 클라이언트로 부터 HTTP요청을 가로채서 JWT토큰 검사하고
 * 유효한 토큰이면 사용자 정보를 SecurityContext에 저장
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    /**
     * 요청 필터링: JWT 토큰을 검증해서 유효하면 사용자 정보 설정
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param chain    필터 체인
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 요청헤더에서 Authorization 추출
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // authorizationHeader가 존재하고, "Bearer "로 시작할 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // "Bearer " 이후의 JWT 토큰 부분
            username = jwtUtil.getUsernameFromToken(jwt); // JWT 토큰에서 사용자 이름 추출
        }

        // 사용자 이름이 존재하고, 현재 SecurityContext에 인증 정보가 없는 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 사용자 정보를 DB에서 조회 (refreshToken을 조회하기 위함)
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String refreshToken = user.getRefreshToken();

                // userStatus가 WITHDRAWN인 경우
                if (user.getUserStatus().equals(UserStatus.WITHDRAWN)) {
                    response.setCharacterEncoding("UTF-8"); // 한국어 설정
                    response.getWriter().write("탈퇴한 회원입니다.");
                    return;
                }
                // refreshToken이 비어있다면 다시 로그인 유도
                if (refreshToken == null) {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("다시 로그인하세요");
                    return;
                }

                // JWT 토큰이 유효한 경우
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        // 다음 필터로 요청을 전달
        chain.doFilter(request, response);
    }
}
