package com4table.ssupetition.domain.post.repository;

import com4table.ssupetition.domain.post.domain.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTypeRepository extends JpaRepository<PostType, Long> {
    Optional<PostType> findByPostTypeName(String name);
}
