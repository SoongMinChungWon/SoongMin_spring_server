package com4table.ssupetition.domain.post.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostAnswer;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
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
        private LocalDateTime createdAt;


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
            this.createdAt = post.getCreatedAt();
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class PostDTO {
        private Long postId;
        private String title;
        private String content;
        private Long agree;
        private Long disagree;
        private Long participants;
        private Long userId; // 또는 String userName; 등 필요한 사용자 정보 추가

        public Post toEntity(Post post) {
            return Post.builder()
                    .user(post.getUser())
                    .postId(post.getPostId())
                    .postCategory(post.getPostCategory())
                    .postType(post.getPostType())
                    .participants(post.getParticipants())
                    .agree(post.getAgree())
                    .disagree(post.getDisagree())
                    .title(post.getTitle())
                    .content(post.getContent())

                    .build();
        }
    }
    public static class SimilarityDTO {
        private Long postId;
        private Double similarity;

        public SimilarityDTO(Long postId, Double similarity) {
            this.postId = postId;
            this.similarity = similarity;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostAnswerDTO {
        private Long postId;
        private Long userId;
        private Category postCategory;
        private Type postType;
        private String title;
        private String content;
        private Long participants;
        private Long agree;
        private Long disagree;
        private LocalDateTime createdAt;
        private String answer;


        public PostAnswerDTO(Post post, PostAnswer postAnswer) {
            this.postId = post.getPostId();
            this.userId = post.getUser().getUserId();
            this.postCategory = post.getPostCategory();
            this.postType = post.getPostType();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.participants = post.getParticipants();
            this.agree = post.getAgree();
            this.disagree = post.getDisagree();
            this.createdAt = post.getCreatedAt();
            this.answer = postAnswer.getPostAnswerContent();
        }
    }
    @Getter
    @Builder
    @AllArgsConstructor
    public static class PostAIDTO {
        private Long postId;
        private Long userId;
        private Category postCategory;
        private Type postType;
        private String title;
        private String content;
        private Long participants;
        private Long agree;
        private Long disagree;
        private LocalDateTime createdAt;
        private String similarity;


        public PostAIDTO(Post post, String similarity) {
            this.postId = post.getPostId();
            this.userId = post.getUser().getUserId();
            this.postCategory = post.getPostCategory();
            this.postType = post.getPostType();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.participants = post.getParticipants();
            this.agree = post.getAgree();
            this.disagree = post.getDisagree();
            this.createdAt = post.getCreatedAt();
            this.similarity = similarity;
        }
    }

}
