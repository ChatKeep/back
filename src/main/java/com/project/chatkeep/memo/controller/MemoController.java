package com.project.chatkeep.memo.controller;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.service.MemberService;
import com.project.chatkeep.memo.dto.MemoRequest;
import com.project.chatkeep.memo.dto.MemoResponse;
import com.project.chatkeep.memo.entity.Memo;
import com.project.chatkeep.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
@Slf4j
public class MemoController {

    private final MemoService memoService;

    private final MemberService memberService;

//    List<Memo> findByMemberId(Long memberid); //메모 리스트 조회
    @GetMapping()
    public ResponseEntity<List<MemoResponse>> getByMemberId(Principal principal){
        Member member = memberService.findLoginMember(principal);

        List<Memo> memoList = memoService.findByMemberId(member.getId());
        List<MemoResponse> memoResponseList = new ArrayList<>();
        for(Memo memo : memoList){
            MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
            memoResponseList.add(memoResponse);
        }
        return ResponseEntity.ok()
                .body(memoResponseList);
    }

    //List<Memo> findByMemberIdAndFolderId(Long memberid, Long folderId); // 맴버의 특정 폴더의 메모 조회
    @GetMapping("folder")
    public ResponseEntity<List<MemoResponse>> getByMemberIdAndFolderId(@RequestParam("folderId") Long folderId, Principal principal){
        Member member = memberService.findLoginMember(principal);

        List<Memo> memoList = memoService.findByMemberIdAndFolderId(member.getId(), folderId);
        List<MemoResponse> memoResponseList = new ArrayList<>();
        for(Memo memo : memoList){
            MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
            memoResponseList.add(memoResponse);
        }
        return ResponseEntity.ok()
                .body(memoResponseList);
    }

//    Optional<Memo> findByID(Long memoId); //메모 조회
    @GetMapping("memo")
    public ResponseEntity<MemoResponse> getMemo(@RequestParam(value = "memoId") Long memoId){
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }


//    Memo save(MemoRequest memoRequest); //메모 등록
    @PostMapping()
    public ResponseEntity<MemoResponse> registMemo(Principal principal){
        Member member = memberService.findLoginMember(principal);
        Memo memo = memoService.save(member);
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }

//     void updateMemoTitle(String title, Long memoId); //메모 제목 수정
    @PatchMapping("update/title")
    public ResponseEntity<MemoResponse> updateMemoTitle(@RequestParam("title") String title, @RequestParam("memoId") Long memoId){
        memoService.updateMemoTitle(title,memoId);
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }

//    void updateMemoContent(String content, Long memoId); //메모 내용 수정
    @PatchMapping("update/content")
    public ResponseEntity<MemoResponse> updateMemoContent(@RequestParam("content") String content, @RequestParam("memoId") Long memoId){
        memoService.updateMemoContent(content,memoId);
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }

//    void updateMemoLink(String link, Long memoId); //메모 링크 수정
    @PatchMapping("update/link")
    public ResponseEntity<MemoResponse> updateMemoLink(@RequestParam("link") String link, @RequestParam("memoId") Long memoId){
        memoService.updateMemoLink(link,memoId);
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }

//    void updateFolderName(Long folderId, Long memoId); // 메모의 폴더 수정
    @PatchMapping("update/folderName")
    public ResponseEntity<MemoResponse> updateFolderName(@RequestParam("folderId") Long folderId, @RequestParam("memoId") Long memoId){
        memoService.updateFolderName(folderId,memoId);
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }


//    void updateBookmark(Long memoId); //북마크 상태 수정
    @PatchMapping("update/bookmark")
    public ResponseEntity<MemoResponse> updateBookmark(@RequestParam("memoId") Long memoId){
        memoService.updateBookmark(memoId);
        Optional<Memo> memoOptional = memoService.findByID(memoId);
        Memo memo = memoOptional.orElseThrow();
        MemoResponse memoResponse = memoEntityToMeNoResponse(memo);
        return ResponseEntity.ok()
                .body(memoResponse);
    }


//    void delete(Long memoId); //메모 삭제
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("memoId") Long memoId){
        memoService.delete(memoId);
        return ResponseEntity.ok()
                .body("메모 삭제 완료");
    }


    private MemoResponse memoEntityToMeNoResponse(Memo memo){
        MemoResponse memoResponse = new MemoResponse();
        memoResponse.setId(memo.getId());
        memoResponse.setTitle(memo.getTitle());
        memoResponse.setContent(memo.getContent());
        memoResponse.setCreatedDate(memo.getCreatedDate());
        memoResponse.setLastModifiedDate(memo.getLastModifiedDate());
        memoResponse.setBookmark(memo.getBookmark());
        memoResponse.setLink(memo.getLink());
        memoResponse.setMemberId(memo.getMemberId().getId());
        if(memo.getFolderId() != null)
            memoResponse.setFolderId(memo.getFolderId().getId());
        return memoResponse;
    }

}
