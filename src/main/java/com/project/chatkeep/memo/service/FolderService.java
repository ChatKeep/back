package com.project.chatkeep.memo.service;

import com.project.chatkeep.memo.dto.FolderRequest;
import com.project.chatkeep.memo.entity.Folder;

import java.util.List;

public interface FolderService {

    List<Folder> findALlByMemberId(Long memberId); //전체 폴더 조회

    Folder findById(Long folderId);//폴더 조회

    Folder save(String folderName, Long memberId); //폴더 등록

    void updateFolderName(Long folderId, String folderName); //풀더명 수정

    void delete(Long folderId); //폴더 삭제


}
