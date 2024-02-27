package com.project.chatkeep.login.entity;

import com.project.chatkeep.oauth.OauthInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String nickname;

    @Embedded
    @Column(unique = true)
    private OauthInfo oauthInfo;

    private String accessToken;

    private String role;

    public Member update(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Member update(String nickname, String profileImage) {
        this.nickname = nickname;
        return this;
    }

}
