package com.project.chatkeep.memo.service;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.memo.dto.MemoRequest;
import com.project.chatkeep.memo.entity.Folder;
import com.project.chatkeep.memo.entity.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoService {

    List<Memo> findByMemberId(Long memberid); // 멤버의 모든메모  조회
    List<Memo> findByMemberIdAndFolderId(Long memberid, Long folderId); // 맴버의 특정 폴더의 메모 조회
    Optional<Memo> findByID(Long memoId); //메모 조회

    //Memo save(MemoRequest memoRequest); //메모 등록

    Memo save(Member member); //메모 등록

    void updateMemoTitle(String title, Long memoId); //메모 제목 수정

    void updateMemoContent(String content, Long memoId); //메모 내용 수정

    void updateMemoLink(String link, Long memoId); //메모 링크 수정

    void updateFolderName(Long folderId, Long memoId); // 메모의 폴더 수정
    void updateBookmark(Long memoId); //북마크 상태 수정
    void delete(Long memoId); //메모 삭제

    //void updateMemo(String title, String content, Long memoId); //메모 수정

}
