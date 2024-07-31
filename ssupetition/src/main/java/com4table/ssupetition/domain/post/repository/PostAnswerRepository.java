package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.PostAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostAnswerRepository extends JpaRepository<PostAnswer, Long> {
    List<PostAnswer> findByPostId_PostId(Long postId);

}
