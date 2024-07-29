package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.EmbeddingValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmbeddingValueRepository extends JpaRepository<EmbeddingValue, Long> {
    List<EmbeddingValue> findByPost_PostId(Long postId);
    void deleteByPost_PostId(Long postId);}