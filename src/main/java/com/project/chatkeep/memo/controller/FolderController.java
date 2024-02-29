package com.project.chatkeep.memo.controller;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.login.service.MemberService;
import com.project.chatkeep.memo.dto.FolderRequest;
import com.project.chatkeep.memo.dto.FolderResponse;
import com.project.chatkeep.memo.dto.MemoResponse;
import com.project.chatkeep.memo.entity.Folder;
import com.project.chatkeep.memo.entity.Memo;
import com.project.chatkeep.memo.service.FolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
@Slf4j
public class FolderController {

    private final FolderService folderService;

    private final MemberService memberService;

//    List<Folder> findFolderNameByMemberId(Long memberId); //전체 폴더 조회
    @GetMapping("folderList")
    public ResponseEntity<List<FolderResponse>> folderList(Principal principal){
        Member loginMember = memberService.findLoginMember(principal);
        List<Folder> folderList = folderService.findALlByMemberId(loginMember.getId());
        List<FolderResponse> folderResponseList = new ArrayList<>();
        for(Folder folder : folderList){
            log.info("folder.toString() {}" , folder.toString());
            FolderResponse folderResponse = folderEntityToFolderResponse(folder);
            folderResponseList.add(folderResponse);
        }
        return ResponseEntity.ok()
                .body(folderResponseList);

    }

//    Folder save(String folderName, Long memberId); //폴더 등록
    @PostMapping
    public ResponseEntity<FolderResponse> registFolder(@RequestBody String folderName, Principal principal){
        Member member = memberService.findLoginMember(principal);
        Folder folder = folderService.save(folderName, member.getId());
        FolderResponse folderResponse = folderEntityToFolderResponse(folder);
        return ResponseEntity.ok()
                .body(folderResponse);

    }

//    void updateFolderName(Long folderId, String folderName); //풀더명 수정
    @PatchMapping
    public ResponseEntity<String> updateFolderName(@RequestParam("folderId") Long folderId, @RequestParam("folderName") String folderName){
        folderService.updateFolderName(folderId, folderName);

        return  ResponseEntity.ok()
                .body("폴더명 수정 완료");
    }

//    void delete(Long folderId); //폴더 삭제
    @DeleteMapping
    public  ResponseEntity<String> delete(@RequestParam("folderId") Long folderId){
        folderService.delete(folderId);
        return ResponseEntity.ok()
                .body("폴더명 삭제 완료");
    }

    public FolderResponse folderEntityToFolderResponse(Folder folder){
        FolderResponse folderResponse = new FolderResponse();
        folderResponse.setId(folder.getId());
        folderResponse.setFolderName(folder.getFolderName());
        folderResponse.setMemberId(folder.getMemberId());
        return folderResponse;
    }

}
