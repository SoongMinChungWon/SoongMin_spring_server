package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.PostCategory;
import com4table.ssupetition.domain.post.domain.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByPostCategoryName(String name);

}
