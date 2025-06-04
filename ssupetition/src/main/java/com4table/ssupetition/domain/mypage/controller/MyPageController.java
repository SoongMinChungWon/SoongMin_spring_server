package com4table.ssupetition.domain.mypage.controller;

import com4table.ssupetition.domain.mypage.domain.WritePost;
import com4table.ssupetition.domain.mypage.service.MyPageService;
import com4table.ssupetition.domain.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "마이페이지 API", description = "마이페이지 API에 대한 설명입니다." )
public class MyPageController {

    private final MyPageService myPageService;
    @Operation(description = "userId를 path value로 입력받으면 사용자가 동의한 게시글들이 리턴됨 ")
    @GetMapping("/agree-posts/{userId}")
    public List<PostResponse.AllListDTO> getAgreePosts(@PathVariable(name = "userId") Long userId) {
        return myPageService.getAgreePosts(userId);
    }

    @Operation(description = "userId를 path value로 입력받으면 사용자가 댓글을 단 게시글들이 리턴됨 ")
    @GetMapping("/comment-posts/{userId}")
    public List<PostResponse.AllListDTO> getCommentPosts(@PathVariable(name = "userId") Long userId) {
        return myPageService.getCommentPosts(userId);
    }


    @Operation(description = "userId를 path value로 입력받으면 사용자가 작성한 게시글들이 리턴됨 ")
    @GetMapping("/write-posts/{userId}")
    public List<PostResponse.AllListDTO> getWritePosts(@PathVariable(name = "userId") Long userId) {
        return myPageService.getWritePosts(userId);
    }
}