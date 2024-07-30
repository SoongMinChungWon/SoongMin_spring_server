package com4table.ssupetition.domain.mypage.controller;

import com4table.ssupetition.domain.mypage.domain.AgreePost;
import com4table.ssupetition.domain.mypage.domain.CommentPost;
import com4table.ssupetition.domain.mypage.domain.WritePost;
import com4table.ssupetition.domain.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/agree-posts/{userId}")
    public List<AgreePost> getAgreePosts(@PathVariable Long userId) {
        return myPageService.getAgreePosts(userId);
    }

    @GetMapping("/comment-posts/{userId}")
    public List<CommentPost> getCommentPosts(@PathVariable Long userId) {
        return myPageService.getCommentPosts(userId);
    }

    @GetMapping("/write-posts/{userId}")
    public List<WritePost> getWritePosts(@PathVariable Long userId) {
        return myPageService.getWritePosts(userId);
    }
}