package src.main.java.com.project.chatkeep.memo.repository;


import hello.src.main.java.com.project.chatkeep.memo.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
