package com4table.ssupetition.domain.mypage.service;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.domain.CommentPost;
import com4table.ssupetition.domain.mypage.repository.AgreePostRepository;
import com4table.ssupetition.domain.mypage.repository.CommentPostRepository;
import com4table.ssupetition.domain.mypage.repository.WritePostRepository;
import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final AgreePostRepository agreePostRepository;
    private final CommentPostRepository commentPostRepository;
    private final WritePostRepository writePostRepository;

    public List<PostResponse.AllListDTO> getAgreePosts(Long userId) {

        List<AgreePost> agreePosts = agreePostRepository.findByUser_UserId(userId);
        return agreePosts.stream()
                .map(agreePost -> new PostResponse.AllListDTO(agreePost.getPost()))
                .collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getCommentPosts(Long userId) {
        List<CommentPost> commentPosts = commentPostRepository.findByUser_UserId(userId);
        return commentPosts.stream()
                .map(commentPost -> new PostResponse.AllListDTO(commentPost.getPost()))
                .collect(Collectors.toList());
    }

    public List<PostResponse.AllListDTO> getWritePosts(Long userId) {
        List<Post> posts = postRepository.findByUser_UserId(userId);
        return posts.stream()
                .map(mypost -> new PostResponse.AllListDTO(mypost))
                .collect(Collectors.toList());
    }
}