package com.project.chatkeep.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chatkeep.login.dto.MemberRequest;
import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public String findIdByToken(Principal principal){
        ObjectMapper objectMapper = new ObjectMapper();
        String principalString = principal.getName();

        principalString = principalString.replaceAll("[{}]", "");
        String[] keyValuePairs = principalString.split(",");
        Map<String, String> map = new HashMap<>();

        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }

        // Id 값 추출
        String idValue = map.get("id");
        log.info("MemberService - findIdByToken - idValue {}", idValue);
        return idValue;
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected member"));
    }

    @Transactional
    public void update(MemberRequest request, Member member) {
        log.info("MemberService - request.getNickname() {}", request.getNickname());
        member.update(request.getNickname());
    }


    public Optional<Member> findByOauthInfo_OauthId(String oauthId){
        return memberRepository.findByOauthInfo_OauthId(oauthId);
    }



}
