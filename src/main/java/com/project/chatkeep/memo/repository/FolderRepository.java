package com.project.chatkeep.memo.repository;

import com.project.chatkeep.memo.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findALlByMemberId(Long memberId);

}
