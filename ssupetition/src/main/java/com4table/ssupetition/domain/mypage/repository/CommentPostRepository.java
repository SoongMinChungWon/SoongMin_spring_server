package com4table.ssupetition.domain.mypage.repository;

import com4table.ssupetition.domain.mypage.domain.CommentPost;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentPostRepository extends JpaRepository<CommentPost, Long> {
    boolean existsByUserAndPost(User user, Post post);
    List<CommentPost> findByUser_UserId(Long user);
}
