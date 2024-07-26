package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
}
