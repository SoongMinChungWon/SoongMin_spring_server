package com4table.ssupetition.domain.comment.repository;

import com4table.ssupetition.domain.comment.domain.Comment;
import com4table.ssupetition.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Post post);

}
