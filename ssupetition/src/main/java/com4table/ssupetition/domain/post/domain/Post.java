package com4table.ssupetition.domain.post.domain;

import com4table.ssupetition.domain.base_time.BaseTimeEntity;
import com4table.ssupetition.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postCategoryId")
    private PostCategory postCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postTypeId")
    private PostType postTypeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Long participants;

    private Long agree;

    private Long disagree;

    private List<Double> embedding;



}
