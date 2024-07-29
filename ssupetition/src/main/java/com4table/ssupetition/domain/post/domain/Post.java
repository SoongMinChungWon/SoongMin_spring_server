package com4table.ssupetition.domain.post.domain;

import com4table.ssupetition.domain.base_time.BaseTimeEntity;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
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
    @JoinColumn(name = "user_id")
    private User user;

    private Category postCategory;

    private Type postType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private Long participants;

    private Long agree;

    private Long disagree;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmbeddingValue> embedding;

    public void setParticipants(Long participants) {
        this.participants = participants;
    }

    public void setAgree(Long agree) {
        this.agree = agree;
    }

    public void setDisagree(Long disagree) {
        this.disagree = disagree;
    }

    public void setPostType(Type postType) {
        this.postType = postType;
    }

}