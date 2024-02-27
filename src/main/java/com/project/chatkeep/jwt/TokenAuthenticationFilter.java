package com.project.chatkeep.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    private final static String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더의 Authorization 키의 값 조회
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        // 가져온 값에서 접두사 제거
        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음
        if(authorizationHeader == null) {

            // 화면 로그인 시 쿠키의 "refresh_token"로 Jwt Token을 전송
            // 쿠키에도 Jwt Token이 없다면 로그인 하지 않은 것으로 간주
            if(request.getCookies() == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 쿠키에서 "refresh_token(Jwt Token)"을 Key로 가진 쿠키를 찾아서 가져오고 없으면 null return
            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME))
                    .findFirst()
                    .orElse(null);

            if(jwtTokenCookie == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 쿠키 jwtToken(name: refreash_Token)이 있다면 이 토큰으로 인증, 인가 진행
            String jwtToken = jwtTokenCookie.getValue();
            authorizationHeader = "Bearer " + jwtToken;
        }

        String token = getAccessToken(authorizationHeader);
        // 가져온 토큰이 유효한지 확인하고, 유효한 때는 인증 정보 설정
        if (tokenProvider.validateToken(token)) {
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}