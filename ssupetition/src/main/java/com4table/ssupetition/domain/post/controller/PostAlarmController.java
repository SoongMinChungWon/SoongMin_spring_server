package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name ="알람 컨트롤러 API입니다.", description = "알람 컨트롤러 API에 대한 설명입니다.")
public class PostAlarmController {
    private final PostService postService;

    @Operation(description = "최근에 메일을 받거나 보낸 게시글에 대해서 리턴하는 API입니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponse.AllListDTO>> getPostsByUserIdAndType(@PathVariable(name = "userId") Long userId) {
        List<PostResponse.AllListDTO> posts = postService.getPostsByUserIdAndType(userId);
        return ResponseEntity.ok(posts);
    }
}
