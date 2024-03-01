package src.main.java.com.project.chatkeep.memo.service;

import hello.src.main.java.com.project.chatkeep.memo.domain.Memo;
import hello.src.main.java.com.project.chatkeep.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public Memo save(Memo memo) {
        return memoRepository.save(memo);
    }

    public void delete(Long id) {
        memoRepository.deleteById(id);
    }

    public List<Memo> findMemos() {
        return memoRepository.findAll();
    }

    public Optional<Memo> findById(Long memoId) {
        return memoRepository.findById(memoId);
    }

    // gpt
    public void bookmarkMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memo Id:" + memoId));
        memo.setBookmarked(true);
        memoRepository.save(memo);
    }

    public void unbookmarkMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memo Id:" + memoId));
        memo.setBookmarked(false);
        memoRepository.save(memo);
    }



}
