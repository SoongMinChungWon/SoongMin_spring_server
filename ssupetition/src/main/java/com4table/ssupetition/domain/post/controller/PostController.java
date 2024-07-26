package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody PostRequest.AddDTO addDTO, @PathVariable(name = "userId") Long userId) {
        Post createdPost = postService.addPost(userId, addDTO);
        return ResponseEntity.ok(createdPost);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removePost(@PathVariable Long postId) {
        postService.removePost(postId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/agree")
    public ResponseEntity<Void> addPostAgree(@PathVariable Long postId) {
        postService.addPostAgree(postId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/disagree")
    public ResponseEntity<Void> addPostDisagree(@PathVariable Long postId) {
        postService.addPostDisagree(postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostList() {
        List<Post> posts = postService.getPostList();
        return ResponseEntity.ok(posts);
    }

//    @GetMapping("/category/{categoryId}")
//    public ResponseEntity<List<Post>> getPostListWithCategory(@PathVariable Long categoryId) {
//        List<Post> posts = postService.getPostListWithCategory(categoryId);
//        return ResponseEntity.ok(posts);
//    }
//
//    @GetMapping("/type/{typeId}")
//    public ResponseEntity<List<Post>> getPostListWithPostType(@PathVariable Long typeId) {
//        List<Post> posts = postService.getPostListWithPostType(typeId);
//        return ResponseEntity.ok(posts);
//    }
}
