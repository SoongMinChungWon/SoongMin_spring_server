package com4table.ssupetition.domain.comment.dto;

import com4table.ssupetition.domain.comment.domain.Comment;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddDTO {
        private String commentContent;

        public Comment toEntity(User user, Post post) {
            return Comment.builder()
                    .commentContent(commentContent)
                    .userId(user)
                    .postId(post)
                    .build();
        }
    }
}