package com.project.chatkeep.memo.repository;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.memo.entity.Folder;
import com.project.chatkeep.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByMemberId(Member memberId);

    List<Memo> findByMemberIdAndFolderId(Member memberId, Folder folderId);  // 맴버의 특정 폴더의 메모 조회

}
