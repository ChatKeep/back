package com.project.chatkeep.memo.entity;

import com.project.chatkeep.login.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@ToString(exclude = "memberId")
public class Memo {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    private Boolean bookmark;

    private String link;

    @ManyToOne
    @JoinColumn( referencedColumnName = "id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Folder folderId;

    public Memo(String title, String content, Boolean bookmark, String link, Member memberId, Folder folderId) {
        this.title = title;
        this.content = content;
        this.bookmark = bookmark;
        this.link = link;
        this.memberId = memberId;
        this.folderId = folderId;
    }

    @Builder
    public Memo(String title, String content, Member memberId, Folder folderId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.folderId = folderId;
    }

    @Builder
    public Memo(String title, String content, Member memberId) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }

    @Builder
    public Memo(Member memberId) {
        this.memberId = memberId;
    }

    @Builder
    public void updateMemo(String title, String content ){
        this.title = title;
        this.content =  content;
    }

    @Builder
    public void updateMemoTitle(String title ){
        this.title = title;
        this.content =  content;
    }

    @Builder
    public void updateMemoContent(String content ){
        this.content =  content;
    }

    @Builder
    public void updateMemoLink(String link ){
        this.link = link;
    }

    @Builder
    public void updateFolderName(Folder folderId){
        this.folderId = folderId;
    }

    @Builder
    public void updateBookmark(Boolean bookmark){
        this.bookmark = bookmark;
    }



}
