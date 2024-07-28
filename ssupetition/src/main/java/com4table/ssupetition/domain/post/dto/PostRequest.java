package com4table.ssupetition.domain.post.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private Category category;
        private Type type;

        public Post toEntity(User user, List<Double> embedding) {
            return Post.builder()
                    .user(user)
                    .postCategory(category)
                    .postType(type)
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
