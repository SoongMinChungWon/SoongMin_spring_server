package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
