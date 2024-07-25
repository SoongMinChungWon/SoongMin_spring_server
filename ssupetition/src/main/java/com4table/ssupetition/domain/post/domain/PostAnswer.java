package com4table.ssupetition.domain.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postAnswerId;

    //게시글 식별자

    private String postAnswerContent;


}
