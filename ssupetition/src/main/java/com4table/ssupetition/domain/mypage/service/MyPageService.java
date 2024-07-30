package com4table.ssupetition.domain.mypage.service;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.domain.CommentPost;
import com4table.ssupetition.domain.mypage.domain.WritePost;
import com4table.ssupetition.domain.mypage.repository.AgreePostRepository;
import com4table.ssupetition.domain.mypage.repository.CommentPostRepository;
import com4table.ssupetition.domain.mypage.repository.WritePostRepository;
import com4table.ssupetition.domain.user.domain.User;
import com4table.ssupetition.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final AgreePostRepository agreePostRepository;
    private final CommentPostRepository commentPostRepository;
    private final WritePostRepository writePostRepository;

    public List<AgreePost> getAgreePosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        return agreePostRepository.findByUser(user);
    }

    public List<CommentPost> getCommentPosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        return commentPostRepository.findByUser(user);
    }

    public List<WritePost> getWritePosts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        return writePostRepository.findByUser(user);
    }
}