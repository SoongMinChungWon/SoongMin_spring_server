package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.repository.AgreePostRepository;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.repository.EmbeddingValueRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;
    private final EmbeddingValueRepository embeddingValueRepository;
    private final AgreePostRepository agreePostRepository;

    public Post addPost(Long userId, PostRequest.AddDTO addDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        List<Double> embedding = new ArrayList<>();
        embedding.add((double) 0.1);
        embedding.add((double) 0.2);
        embedding.add((double) 0.3);

        Post post = addDTO.toEntity(user, embedding);
        return postRepository.save(post);
    }
    public void removePost(Long postId, Long userId) {
        embeddingValueRepository.deleteByPost_PostId(postId); // embedding_values 테이블에서 삭제
        postRepository.deleteById(postId);
    }

    public Post addPostAgree(Long postId,Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(checkParticipate(postId, userId, post, user, false)){
            return null;
        }
        post.setAgree(post.getAgree() + 1);
        post.setParticipants(post.getParticipants()+1);
        return postRepository.save(post);
    }

    public Post addPostDisagree(Long postId,Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if(checkParticipate(postId, userId, post, user, false)){
            return null;
        }
        post.setDisagree(post.getDisagree() + 1);
        post.setParticipants(post.getParticipants()+1);
        return postRepository.save(post);
    }

    private boolean checkParticipate(Long postId, Long userId, Post post, User user, boolean state) {
        Optional<AgreePost> existingLike = agreePostRepository.findByPost_PostIdAndUser_UserId(postId, userId);

        if (existingLike.isPresent()) {
            return true;
        }

        AgreePost postLike = AgreePost.builder()
                .post(post)
                .user(user)
                .state(state)
                .build();
        agreePostRepository.save(postLike);

        return false;
    }

    public List<PostResponse.AllListDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree() {
        List<Post> posts = postRepository.findAllOrderByAgreeDesc();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry() {
        List<Post> posts = postRepository.findAllOrderByExpiryAsc();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate() {
        List<Post> posts = postRepository.findAllOrderByCreatedDateDesc();
        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private PostResponse.AllListDTO convertToDto(Post post) {
        User user = userRepository.findById(post.getUser().getUserId()).orElseThrow();

        return PostResponse.AllListDTO.builder()
                .postId(post.getPostId())
                .userId(user.getUserId())
                .postCategory(post.getPostCategory())
                .postType(post.getPostType())
                .title(post.getTitle())
                .content(post.getContent())
                .participants(post.getParticipants())
                .agree(post.getAgree())
                .disagree(post.getDisagree())
                .embedding(post.getEmbedding())
                .build();
    }

    //최다동의순
    //만료임박순
    //최신순
    //알림 만들어야됨

}


