package com4table.ssupetition.domain.post.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import lombok.*;

import java.util.List;

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

        public PostResponse.AllListDTO toEntity(Post post, User user) {
            return AllListDTO.builder()
                    .postId(post.getPostId())
                    .userId(user.getUserId())
                    .postCategory(postCategory)
                    .postType(postType)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .participants(post.getParticipants())
                    .agree(post.getAgree())
                    .disagree(post.getDisagree())
                    .embedding(post.getEmbedding())
                    .build();


        }
    }
}
