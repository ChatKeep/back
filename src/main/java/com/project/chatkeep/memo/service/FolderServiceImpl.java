package com.project.chatkeep.memo.service;

import com.project.chatkeep.memo.dto.FolderRequest;
import com.project.chatkeep.memo.entity.Folder;
import com.project.chatkeep.memo.repository.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class FolderServiceImpl implements FolderService{

    private final FolderRepository folderRepository;
    @Override
    public List<Folder> findALlByMemberId(Long memberId) {
        return folderRepository.findALlByMemberId(memberId);
    }

    @Override
    public Folder findById(Long folderId) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        Folder folder = folderOptional.orElseThrow();
        return folder;
    }

    @Override
    public Folder save(String folderName, Long memberId) {
        Folder folder = new Folder(folderName.trim(), memberId);

        return folderRepository.save(folder);
    }

    @Override
    public void updateFolderName(Long folderId, String folderName) {
        Optional<Folder> folderOptional = folderRepository.findById(folderId);
        Folder folder = folderOptional.orElseThrow();
        folder.updateFolderName(folderName.trim());
    }

    @Override
    public void delete(Long folderId) {
        folderRepository.deleteById(folderId);
    }
}
