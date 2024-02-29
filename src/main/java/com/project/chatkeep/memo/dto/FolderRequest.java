package com.project.chatkeep.memo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FolderRequest {

    private Long id;

    private String folderName;

    private Long memberId;

}
