package com.project.chatkeep.oauth;

import com.project.chatkeep.login.dto.MemberResponse;
import com.project.chatkeep.login.entity.MsgEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class OauthController {

    private final OauthService oauthService;

    @GetMapping()
    public String goLoginPage(){
        return oauthService.getKakaoLoginPage();
    }

    @PostMapping("/kakao")
    public ResponseEntity<MsgEntity> kakaoLogin(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        MemberResponse memberResponse = oauthService.kakaoLogin(code, request, response);
        return ResponseEntity.ok()
                .body(new MsgEntity("Success", memberResponse));
    }


}
