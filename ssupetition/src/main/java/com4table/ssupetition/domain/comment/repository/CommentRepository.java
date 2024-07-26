package com4table.ssupetition.domain.comment.repository;

import com4table.ssupetition.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
