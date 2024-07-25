package com4table.ssupetition.domain.post.domain;

import com4table.ssupetition.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId")
    private Post postId;

    private String postAnswerContent;


}
