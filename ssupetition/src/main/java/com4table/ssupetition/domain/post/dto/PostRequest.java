package com4table.ssupetition.domain.post.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostCategory;
import com4table.ssupetition.domain.post.domain.PostType;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class PostRequest {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddDTO {
        private String title;
        private String content;
        private Long categoryId;
        private Long typeId;

        public Post toEntity(User user, PostCategory postCategory, PostType postType, List<Double> embedding) {
            return Post.builder()
                    .user(user)
                    .postCategory(postCategory)
                    .postType(postType)
                    .participants(0L)
                    .agree(0L)
                    .disagree(0L)
                    .title(title)
                    .content(content)
                    .embedding(embedding)
                    .build();
        }
    }
}
