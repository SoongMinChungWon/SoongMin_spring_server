package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.dto.PostResponse;
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
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<Void> removePost(@PathVariable(name="postId") Long postId, @PathVariable(name="userId")Long userId) {
        postService.removePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/agree/{userId}")
    public ResponseEntity<Post> addPostAgree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        postService.addPostAgree(postId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/disagree/{userId}")
    public ResponseEntity<Post> addPostDisagree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        postService.addPostDisagree(postId, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<PostResponse.AllListDTO> getPostList() {
        return postService.getAllPosts();
    }

    @GetMapping("/sorted/agree")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree() {
        return postService.getAllPostsSortedByAgree();
    }

    @GetMapping("/sorted/agree/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByAgree(category, type);
    }


    @GetMapping("/sorted/expiry")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry() {
        return postService.getAllPostsSortedByExpiry();
    }
    @GetMapping("/sorted/expiry/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByExpiry(category, type);
    }

    @GetMapping("/sorted/createdDate")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate() {
        return postService.getAllPostsSortedByCreatedDate();
    }

    @GetMapping("/sorted/createdDate/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByCreatedDate(category, type);
    }

}
