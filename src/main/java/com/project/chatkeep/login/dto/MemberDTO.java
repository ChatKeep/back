package com.project.chatkeep.login.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDTO {

    private Long id;
    private String nickname;
}
