package com4table.ssupetition.domain.mypage.domain;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgreePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agreePostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    private boolean state;

}
