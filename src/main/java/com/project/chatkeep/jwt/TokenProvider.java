package com.project.chatkeep.jwt;

import com.project.chatkeep.login.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class TokenProvider {

    private final JwtProperties jwtProperties;
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), member);
    }

    //jwt 토큰 생성
    private String makeToken(Date expiry, Member member) {
        Date now = new Date();
        String id = member.getOauthInfo().getOauthId();

        Claims claims = Jwts.claims(); //일종의 map
        claims.put("id", id);
        claims.put("auth", "ROLE_USER");
        claims.put("nickname", member.getNickname());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)   // 헤더 typ(타입) : JWT
                .setIssuedAt(now)                               // 내용 iat(발급 일시) : 현재 시간
                .setExpiration(expiry)                          // 내용 exp(만료일시) : expiry 멤버 변수값
                .setSubject(id)     // 내용 sub(토큰 제목) : 회원 ID
                .setClaims(claims)              // 클레임 id : 회원 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }

    //jwt 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())   // 비밀값으로 복호화
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰이다.
            return false;
        }
    }

    //토큰 기반으로 인증 정보를 가져오기
    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        //return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token)
        //        .getBody().get("id", String.class);
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        log.info("TokenProvider - getAuthentication - claims.getSubject() {}" , claims);
        return new UsernamePasswordAuthenticationToken(claims, token, authorities);
    }

    public  String getUserIdFromToken(String token) {
        String result = Jwts.parserBuilder().setSigningKey(Base64.getDecoder().decode(jwtProperties.getSecret()))
                .build().parseClaimsJws(token).getBody().get("id", String.class);
        log.info("TokenProvider - getUserIdFromToken - claims {}" , result);
        return result;
    }

    //토큰에서 회원 id 가져오기
    public String getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", String.class);
    }

    //파라미터로 받은 토큰에서 회원 id 가져오기
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
