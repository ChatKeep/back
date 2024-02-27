package com.project.chatkeep.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatkeep.jwt.TokenProvider;
import com.project.chatkeep.login.dto.MemberResponse;
import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.entity.RefreshToken;
import com.project.chatkeep.login.repository.MemberRepository;
import com.project.chatkeep.login.repository.RefreshTokenRepository;
import com.project.chatkeep.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthService {

    private final MemberRepository memberRepository;

    private final TokenProvider tokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;

    @Value("${kakao.redirect.uri}")
    private String REDIRECT_URI;

    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;

    @Value("${jwt.secret}")
    private String ACCESS_HEADER;

    private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";


    @Transactional
    public MemberResponse kakaoLogin(String code, HttpServletRequest request, HttpServletResponse response) {
        log.info("OauthService - code {}",code);
        // 1. 인가 코드로 OAuth2 액세스 토큰 요청
        String oauthAccessToken = getAccessToken(code);
        // 2. OAuth2 액세스 토큰으로 회원 정보 요청
        JsonNode responseJson = getKakaoUserInfo(oauthAccessToken);

        // 3. 회원 정보 저장
        Member member = registerKakaoUser(responseJson, oauthAccessToken);

        // 4. JWT 액세스 토큰 발급
        String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_DURATION);
        response.setHeader(ACCESS_HEADER, accessToken);

        // 5. JWT 리프레시 토큰 발급
        String refreshToken = tokenProvider.generateToken(member, REFRESH_TOKEN_DURATION);
        saveRefreshToken(member.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);

        MemberResponse memberResponse = new MemberResponse();
        OauthInfo oauthInfo = member.getOauthInfo();
        memberResponse.setKakao_id(oauthInfo.getOauthId());
        memberResponse.setNickname(member.getNickname());
        log.info("OauthService - memberResponse {}",memberResponse);
        return memberResponse;
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addHttpOnlyCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }


    private void saveRefreshToken(Long memberId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(memberId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    //인가 코드로 카카오 서버에 액세스 토큰을 요청
    private String getAccessToken(String code) {
        log.info("OauthService - getAccessToken - code {}",code);
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_AUTH_URI + "/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                String.class
        );


        // HTTP 응답에서 액세스 토큰 꺼내기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("OauthService - getAccessToken - jsonNode.get(\"access_token\").asText() {}",jsonNode.get("access_token").asText());
        return jsonNode.get("access_token").asText();
    }

    //액세스 토큰으로 카카오 서버에 회원 정보를 요청
    private JsonNode getKakaoUserInfo(String accessToken) {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                userInfoRequest,
                String.class
        );

        // HTTP 응답 반환
        String responseBody = response.getBody();
        log.info("OauthService - getKakaoUserInfo - response.getBody() {}",response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //카카오 회원 정보 데이터베이스에 저장
    private Member registerKakaoUser(JsonNode responseJson, String accessToken) {
        String oauthId = responseJson.get("id").asText();
        JsonNode profile = responseJson.get("kakao_account").get("profile");
        String nickname = profile.get("nickname").asText();

        log.info("OauthService - registerKakaoUser oauthId {}",oauthId);
        log.info("OauthService - registerKakaoUser profile {}",profile);
        log.info("OauthService - registerKakaoUser nickname {}",nickname);

        OauthInfo oauthInfo = new OauthInfo(oauthId, OauthProvider.KAKAO);

        Member member = memberRepository.findByOauthInfo(oauthInfo)
                .map(entity -> entity.update(accessToken))
                .orElse(Member.builder()
                        .accessToken(accessToken)
                        .role("ROLE_ADMIN")
                        .nickname(nickname)
                        .oauthInfo(oauthInfo)
                        .build());
        return memberRepository.save(member);
    }

}
