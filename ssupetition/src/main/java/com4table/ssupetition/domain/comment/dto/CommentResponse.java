package com4table.ssupetition.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private Long postId;
    private String commentContent;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}