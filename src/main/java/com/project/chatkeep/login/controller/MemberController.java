package com.project.chatkeep.login.controller;

import com.project.chatkeep.login.dto.MemberDTO;
import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //jwt 토큰 값에 담긴 카카오 id -> member pk id
    @GetMapping("/api/member")
    public ResponseEntity<MemberDTO> getMember(Principal principal) {
        String memberOautoId =  memberService.findIdByToken(principal);

        Optional<Member> memberOptional = memberService.findByOauthInfo_OauthId(memberOautoId);
        Member member = memberOptional.orElseThrow(() -> new NoSuchElementException("Member not found"));
        MemberDTO memberDTO = MemberDTO.builder()
                .id(String.valueOf(member.getId()))
                .nickname(member.getNickname())
                .build();

        return ResponseEntity.ok()
                .body(memberDTO);

    }

}
