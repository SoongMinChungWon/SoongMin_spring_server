package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    List<Post> findByPostCategoryId(Long categoryId);
//    List<Post> findByPostTypeId(Long typeId);
}
