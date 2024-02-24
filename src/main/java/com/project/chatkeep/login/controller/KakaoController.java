package com.project.chatkeep.login.controller;

import com.project.chatkeep.login.dto.MemberDTO;
import com.project.chatkeep.login.entity.MsgEntity;
import com.project.chatkeep.login.service.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("oauth2")
public class KakaoController {

    private final KakaoService kakaoService;

    @GetMapping("/code/kakao")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        MemberDTO kakaoInfo = kakaoService.getKakaoInfo(request.getParameter("code"));

        return ResponseEntity.ok()
                .body(new MsgEntity("Success", kakaoInfo));
    }
}
