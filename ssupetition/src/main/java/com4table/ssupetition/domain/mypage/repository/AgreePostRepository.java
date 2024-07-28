package com4table.ssupetition.domain.mypage.repository;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreePostRepository extends JpaRepository<AgreePost, Long> {
    Optional<AgreePost> findByPost_PostIdAndUser_UserId(Long postId, Long userId);

}
