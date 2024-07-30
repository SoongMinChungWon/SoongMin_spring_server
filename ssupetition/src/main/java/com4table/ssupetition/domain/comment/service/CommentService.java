package com4table.ssupetition.domain.comment.service;

import com4table.ssupetition.domain.comment.domain.Comment;
import com4table.ssupetition.domain.comment.dto.CommentRequest;
import com4table.ssupetition.domain.comment.dto.CommentResponse;
import com4table.ssupetition.domain.comment.repository.CommentRepository;
import com4table.ssupetition.domain.mypage.domain.CommentPost;
import com4table.ssupetition.domain.mypage.repository.CommentPostRepository;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentPostRepository commentPostRepository;

    public Comment addComment(Long userId, Long postId, CommentRequest.AddDTO addDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));

        Comment comment = addDTO.toEntity(user, post);
        Comment savedComment = commentRepository.save(comment);
        // CommentPost 엔티티가 있는지 확인하고 없으면 추가
        boolean exists = commentPostRepository.existsByUserAndPost(user, post);
        if (!exists) {
            CommentPost commentPost = CommentPost.builder()
                    .user(user)
                    .post(post)
                    .comment(savedComment)
                    .build();
            commentPostRepository.save(commentPost);
        }
        return savedComment;
    }

    public void removeComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + postId));
        List<Comment> comments = commentRepository.findByPostId(post);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentResponse convertToDto(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .userId(comment.getUserId().getUserId())
                .postId(comment.getPostId().getPostId())
                .commentContent(comment.getCommentContent())
                .createdDate(comment.getCreatedAt())
                .build();
    }
}
