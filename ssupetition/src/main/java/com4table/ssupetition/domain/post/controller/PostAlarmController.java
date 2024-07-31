package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alarm")
@RequiredArgsConstructor
public class PostAlarmController {
    private final PostService postService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponse.AllListDTO>> getPostsByUserIdAndType(@PathVariable(name = "userId") Long userId) {
        List<PostResponse.AllListDTO> posts = postService.getPostsByUserIdAndType(userId);
        return ResponseEntity.ok(posts);
    }
}
