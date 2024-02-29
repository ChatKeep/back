package com.project.chatkeep.memo.dto;

import com.project.chatkeep.memo.entity.Folder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemoRequest {

    private String title;

    private String content;

    private Long memberId;

    private Long folderId;

}
