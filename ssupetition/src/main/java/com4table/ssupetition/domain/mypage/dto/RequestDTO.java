package com4table.ssupetition.domain.mypage.dto;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RequestDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreePostDTO {
        private Long userId;
        private Long postId;
    }
}
