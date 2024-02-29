package com.project.chatkeep.memo.dto;

import com.project.chatkeep.login.entity.Member;
import com.project.chatkeep.memo.entity.Folder;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemoResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private Boolean bookmark;

    private String link;

    private Long memberId;

    private Long folderId;

}
