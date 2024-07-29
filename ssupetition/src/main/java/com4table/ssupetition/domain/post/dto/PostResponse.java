package com4table.ssupetition.domain.post.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import lombok.*;

import java.util.List;

@NoArgsConstructor
public class PostResponse {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllListDTO {
        private Long postId;
        private Long userId;
        private Category postCategory;
        private Type postType;
        private String title;
        private String content;
        private Long participants;
        private Long agree;
        private Long disagree;
        private List<Double> embedding;

        public AllListDTO(Post post) {
            this.postId = post.getPostId();
            this.userId = post.getUser().getUserId();
            this.postCategory = post.getPostCategory();
            this.postType = post.getPostType();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.participants = post.getParticipants();
            this.agree = post.getAgree();
            this.disagree = post.getDisagree();
        }
    }
}
