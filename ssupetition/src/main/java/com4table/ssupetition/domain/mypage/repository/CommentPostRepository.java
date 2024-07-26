package com4table.ssupetition.domain.mypage.repository;

import com4table.ssupetition.domain.mypage.domain.CommentPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentPostRepository extends JpaRepository<CommentPost, Long> {
}
