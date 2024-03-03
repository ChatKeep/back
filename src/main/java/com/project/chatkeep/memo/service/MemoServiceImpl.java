package com.project.chatkeep.memo.service;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.repository.MemberRepository;
import com.project.chatkeep.memo.dto.MemoRequest;
import com.project.chatkeep.memo.entity.Folder;
import com.project.chatkeep.memo.entity.Memo;
import com.project.chatkeep.memo.repository.FolderRepository;
import com.project.chatkeep.memo.repository.MemoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MemoServiceImpl implements MemoService{

    private final MemberRepository memberRepository;

    private final MemoRepository memoRepository;

    private final FolderRepository folderRepository;

    @Override
    public List<Memo> findByMemberId(Long memberid) {
        Optional<Member> memberOptional = memberRepository.findById(memberid);
        Member member = memberOptional.orElseThrow();
        return memoRepository.findByMemberId(member);
    }

    @Override
    public List<Memo> findByMemberIdAndFolderId(Long memberid, Long folderId) {
        Optional<Member> memberOptional = memberRepository.findById(memberid);
        Member member = memberOptional.orElseThrow();

        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        Folder folder = folderOptional.orElseThrow();

        return memoRepository.findByMemberIdAndFolderId(member, folder);
    }

    @Override
    public Optional<Memo> findByID(Long memoId) {
        return memoRepository.findById(memoId);
    }

    @Override
    public List<Memo> findByMemberIdAndBookmarkTrue(Long memberid) {
        Optional<Member> memberOptional = memberRepository.findById(memberid);
        Member member = memberOptional.orElseThrow();
        return memoRepository.findByMemberIdAndBookmarkTrue(member);
    }

    @Override
    public Memo save(Member member) { // -> dto로 수정

        Memo memo = new Memo("", "",false, "",member,null);
        return memoRepository.save(memo);
    }

    @Override
    public void updateMemoTitle(String title, Long memoId) {
        Optional<Memo> result = memoRepository.findById(memoId);
        Memo findMemo = result.orElseThrow();

        findMemo.updateMemoTitle(title);
    }

    @Override
    public void updateMemoContent(String content, Long memoId) {
        Optional<Memo> result = memoRepository.findById(memoId);
        Memo findMemo = result.orElseThrow();

        findMemo.updateMemoContent(content);
    }

    @Override
    public void updateMemoLink(String link, Long memoId) {
        Optional<Memo> result = memoRepository.findById(memoId);
        Memo findMemo = result.orElseThrow();

        findMemo.updateMemoLink(link);
    }

    @Override
    public void updateFolderName(Long folderId, Long memoId) {
        Optional<Memo> memoOptional = memoRepository.findById(memoId);
        Memo findMemo = memoOptional.orElseThrow();

        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        Folder folder = folderOptional.orElseThrow();

        findMemo.updateFolderName(folder);
    }

    @Override
    public void updateBookmark(Long memoId) {
        Optional<Memo> memoOptional = memoRepository.findById(memoId);
        Memo findMemo = memoOptional.orElseThrow();

        Boolean bookmarkState = findMemo.getBookmark();

        if(bookmarkState == null || !bookmarkState)
            findMemo.updateBookmark(true);
        else
            findMemo.updateBookmark(false);

    }

    @Override
    public void delete(Long memoId) {
        memoRepository.deleteById(memoId);
    }
}
