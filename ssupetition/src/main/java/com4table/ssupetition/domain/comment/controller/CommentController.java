package com4table.ssupetition.domain.comment.controller;

import com4table.ssupetition.domain.comment.domain.Comment;
import com4table.ssupetition.domain.comment.dto.CommentDto;
import com4table.ssupetition.domain.comment.dto.CommentRequest;
import com4table.ssupetition.domain.comment.dto.CommentResponse;
import com4table.ssupetition.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 작성 API", description = "댓글 작성 API에 대한 설명입니다.")
public class CommentController {

    private final CommentService commentService;

    @Operation(description = "게시글 ID와 유저 ID를 path value로 넣어서 댓글 저장")
    @PostMapping("/add/{postId}/{userId}")
    public ResponseEntity<Comment> addComment(@PathVariable(name = "userId") Long userId, @PathVariable(name = "postId") Long postId, @RequestBody
        CommentDto commentDto) {
        Comment commentResponse = commentService.addComment(userId, postId, commentDto);
        return ResponseEntity.ok(commentResponse);
    }

    @Operation(description = "댓글 Id를 입력 받아서 댓글 삭제")
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> removeComment(@PathVariable(name = "commentId") Long commentId) {
        commentService.removeComment(commentId);
        return ResponseEntity.noContent().build();
    }
    @Operation(description = "postId를 통해서 댓글 조회")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable(name = "postId")Long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }
}
