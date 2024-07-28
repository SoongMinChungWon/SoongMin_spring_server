package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.EmbeddingValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbeddingValueRepository extends JpaRepository<EmbeddingValue, Long> {
    void deleteByPost_PostId(Long postId);
}