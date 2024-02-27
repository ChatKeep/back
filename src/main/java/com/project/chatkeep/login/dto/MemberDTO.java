package com.project.chatkeep.login.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberDTO {

    private String id;
    private String nickname;
}
