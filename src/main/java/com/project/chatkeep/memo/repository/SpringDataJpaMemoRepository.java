package src.main.java.com.project.chatkeep.memo.repository;

import hello.src.main.java.com.project.chatkeep.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaMemoRepository extends JpaRepository<Memo,
        Long>, MemoRepository {
    List<Memo> findByTitleContaining(String title);

    // 예: 북마크된 메모만 찾는 메소드
    List<Memo> findByIsBookmarkedTrue();
}