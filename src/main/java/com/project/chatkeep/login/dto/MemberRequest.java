package com.project.chatkeep.login.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberRequest {

    private String kakao_id;
    private String nickname;
}

