package com.project.chatkeep.memo.entity;

import com.project.chatkeep.login.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@ToString(exclude = "memo")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String folderName;

    private Long memberId;

    @Builder.Default
    @OneToMany(mappedBy = "folderId",cascade = CascadeType.REMOVE)
    private List<Memo> memo = new ArrayList<>();

    @Builder
    public Folder(String folderName, Long memberId) {
        this.folderName = folderName;
        this.memberId = memberId;
    }

    @Builder
    public void updateFolderName(String folderName){
        this.folderName = folderName;
    }

}
