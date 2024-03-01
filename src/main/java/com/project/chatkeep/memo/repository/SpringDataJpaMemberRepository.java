package src.main.java.com.project.chatkeep.memo.repository;

import hello.src.main.java.com.project.chatkeep.memo.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
        Long>, MemberRepository {
    Optional<Member> findByName(String name);
}