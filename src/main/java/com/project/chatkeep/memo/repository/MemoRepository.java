package src.main.java.com.project.chatkeep.memo.repository;



import hello.src.main.java.com.project.chatkeep.memo.domain.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoRepository {

    Memo save(Memo memo);
    Optional<Memo> findById(Long id);
    List<Memo> findAll();
    void deleteById(Long id);
}
