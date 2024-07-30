package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.mail.service.MailService;
import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.domain.WritePost;
import com4table.ssupetition.domain.mypage.repository.AgreePostRepository;
import com4table.ssupetition.domain.mypage.repository.CommentPostRepository;
import com4table.ssupetition.domain.mypage.repository.WritePostRepository;
import com4table.ssupetition.domain.post.domain.EmbeddingValue;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.enums.Category;
import com4table.ssupetition.domain.post.enums.Type;
import com4table.ssupetition.domain.post.repository.EmbeddingValueRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;
    private final EmbeddingValueRepository embeddingValueRepository;
    private final AgreePostRepository agreePostRepository;
    private final MailService emailService;
    private final WritePostRepository writePostRepository;
    private final CommentPostRepository commentPostRepository;


    public Post addPost(Long userId, PostRequest.AddDTO addDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        Post post = addDTO.toEntity(user);
        Post savedPost = postRepository.save(post);

        List<EmbeddingValue> embeddings = new ArrayList<>();
        embeddings.add(new EmbeddingValue(null, post, 0.1));
        embeddings.add(new EmbeddingValue(null, post, 0.2));
        embeddings.add(new EmbeddingValue(null, post, 0.3));

        embeddingValueRepository.saveAll(embeddings);

        WritePost writePost = WritePost.builder()
                .user(user)
                .post(savedPost)
                .build();
        writePostRepository.save(writePost);

        return savedPost;
    }
    public void removePost(Long postId, Long userId) {
        embeddingValueRepository.deleteByPost_PostId(postId);
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

        float agreeRate = (float) post.getAgree() / post.getParticipants();

        boolean sendEmail = checkAgreeCountAndSendEmail(agreeRate, post.getAgree(), post.getTitle(), post.getContent());
        checkChangeType(postId, sendEmail);

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
        checkChangeType(postId, false);

        return postRepository.save(post);
    }

    public void checkChangeType(Long postId, boolean sendEmail){
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));

        if(post.getPostType()==Type.state1 && post.getParticipants()>5){
            post.setPostType(Type.state2);
        }
        else if(post.getPostType()==Type.state2 && sendEmail){

        }
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

    public boolean checkAgreeCountAndSendEmail(float agreeRate, long participate, String title, String content) {
        if (participate>10 && agreeRate >= 2) {
            String to = "ssupetition@gmail.com";
            String subject = title;
            String text = "이 메일로 답신 부탁드립니다.\n" + content;
            emailService.sendSimpleMessage(to, subject, text);

            return true;
        }
        return false;
    }

    public List<PostResponse.AllListDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        //System.out.println(posts);
        posts.forEach(post -> log.info(post.toString()));
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


    // 정렬 메서드 추가
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree(String category, String type) {
        Category categoryEnum = Category.valueOf(category);
        Type typeEnum = Type.valueOf(type);
        List<Post> posts = postRepository.findByPostCategoryAndPostTypeOrderByAgreeDesc(categoryEnum, typeEnum);
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry(String category, String type) {
        Category categoryEnum = Category.valueOf(category);
        Type typeEnum = Type.valueOf(type);
        List<Post> posts = postRepository.findByPostCategoryAndPostTypeOrderByCreatedAtAsc(categoryEnum, typeEnum);
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate(String category, String type) {
        Category categoryEnum = Category.valueOf(category);
        Type typeEnum = Type.valueOf(type);
        List<Post> posts = postRepository.findByPostCategoryAndPostTypeOrderByCreatedAtDesc(categoryEnum, typeEnum);
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    private PostResponse.AllListDTO convertToDto(Post post) {
        User user = userRepository.findById(post.getUser().getUserId()).orElseThrow();

        List<Double> embeddings = embeddingValueRepository.findByPost_PostId(post.getPostId())
                .stream().map(EmbeddingValue::getValue).collect(Collectors.toList());

        log.info("1:{},2:{}", post.getPostCategory(), post.getPostType());
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
                .embedding(embeddings)
                .build();
    }

}


