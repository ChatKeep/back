package com.project.chatkeep.login.repository;

import com.project.chatkeep.oauth.OauthInfo;
import com.project.chatkeep.login.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthInfo(OauthInfo oauthInfo);

    Optional<Member> findByOauthInfo_OauthId(String oauthInfoAuthId);

}
