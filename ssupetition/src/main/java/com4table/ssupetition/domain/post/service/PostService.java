package com4table.ssupetition.domain.post.service;

import com4table.ssupetition.domain.post.controller.PostController;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.domain.PostCategory;
import com4table.ssupetition.domain.post.domain.PostType;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.repository.PostCategoryRepository;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.post.repository.PostTypeRepository;
import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private  final PostTypeRepository postTypeRepository;
    private final UserRepository userRepository;

    public Post addPost(Long userId, PostRequest.AddDTO addDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        PostCategory postCategory = postCategoryRepository.findById(addDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + addDTO.getCategoryId()));
        PostType postType = postTypeRepository.findById(addDTO.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid type ID: " + addDTO.getTypeId()));

        Post post = addDTO.toEntity(user, postCategory, postType);
        return postRepository.save(post);
    }
    public void removePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void addPostAgree(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setAgree(post.getAgree() + 1);
        postRepository.save(post);
    }

    public void addPostDisagree(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setDisagree(post.getDisagree() + 1);
        postRepository.save(post);
    }

    public List<Post> getPostList() {
        return postRepository.findAll();
    }

//    public List<Post> getPostListWithCategory(Long categoryId) {
//        return postRepository.findByPostCategoryId(categoryId);
//    }
//
//    public List<Post> getPostListWithPostType(Long typeId) {
//        return postRepository.findByPostTypeId(typeId);
//    }

    //최다동의순
    //만료임박순
    //최신순
    //알림 만들어야됨

}


