package com.project.chatkeep.oauth;

import com.project.chatkeep.login.dto.MemberResponse;
import com.project.chatkeep.login.entity.MsgEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/kakao")
    public ResponseEntity<MsgEntity> kakaoLogin(HttpServletRequest request, HttpServletResponse response) {
        MemberResponse memberResponse = oauthService.kakaoLogin(request.getParameter("code"), request, response);
        return ResponseEntity.ok()
                .body(new MsgEntity("Success", memberResponse));
    }


}
