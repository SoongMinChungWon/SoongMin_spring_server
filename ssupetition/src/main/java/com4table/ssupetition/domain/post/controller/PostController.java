package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.service.PostAnswerService;
import com4table.ssupetition.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostAnswerService postAnswerService;

    @PostMapping("/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody PostRequest.AddDTO addDTO, @PathVariable(name = "userId") Long userId) {
        Post createdPost = postService.addPost(userId, addDTO);
        return ResponseEntity.ok(createdPost);
    }

    //전체 검색
    @PostMapping("/search")
    public List<PostResponse.AllListDTO> postSearch(@RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        return postService.searchPosts(keyword);
    }

    @PostMapping("/search/sorted-by-agree/{category}")
    public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByAgree(
            @PathVariable(name="category") String category,
            @RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByAgree(category, keyword);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/search/sorted-by-expiry/{category}")
    public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByExpiry(
            @PathVariable(name="category") String category,
            @RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByExpiry(category, keyword);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search/sorted-by-created-date/{category}")
    public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByCreatedDate(
            @PathVariable(name="category") String category,
            @RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByCreatedDate(category, keyword);
        return ResponseEntity.ok(posts);
    }
    @PostMapping("/ai")
    public List<PostResponse.PostAIDTO> postSimilarity(@RequestBody PostRequest.AddDTO addDTO) {
        List<PostResponse.PostAIDTO> similarPost = postService.getSimilarPost(addDTO);
        return similarPost;
    }
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<Void> removePost(@PathVariable(name="postId") Long postId, @PathVariable(name="userId")Long userId) {
        postService.removePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/agree/{userId}")
    public ResponseEntity<PostResponse.AllListDTO> addPostAgree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        PostResponse.AllListDTO allListDTO = postService.addPostAgree(postId, userId);
        if(allListDTO==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(allListDTO);
    }

    @PostMapping("/{postId}/disagree/{userId}")
    public ResponseEntity<PostResponse.AllListDTO> addPostDisagree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        PostResponse.AllListDTO allListDTO = postService.addPostDisagree(postId, userId);
        if(allListDTO==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(allListDTO);
    }

    @GetMapping
    public List<PostResponse.AllListDTO> getPostList() {
        return postService.getAllPosts();
    }

    @GetMapping("/sorted/agree")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree() {
        return postService.getAllPostsSortedByAgree();
    }
    @GetMapping("/sorted/agree/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgreeExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByAgreeExceptCategory(type);
    }
    @GetMapping("/sorted/agree/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByAgree(category, type);
    }
    @GetMapping("/sorted/agree/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgreeState1AndState2() {
        return postService.getAllPostsSortedByAgreeForState1AndState2();
    }


    @GetMapping("/sorted/expiry")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry() {
        return postService.getAllPostsSortedByExpiry();
    }
    @GetMapping("/sorted/expiry/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiryExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByExpiryExceptCategory( type);
    }

    @GetMapping("/sorted/expiry/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByExpiry(category, type);
    }
    @GetMapping("/sorted/expiry/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiryState1AndState2() {
        return postService.getAllPostsSortedByExpiryForState1AndState2();
    }

    @GetMapping("/sorted/createdDate")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate() {
        return postService.getAllPostsSortedByCreatedDate();
    }

    @GetMapping("/sorted/createdDate/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDateExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByCreatedDateExceptCategory(type);
    }

    @GetMapping("/sorted/createdDate/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByCreatedDate(category, type);
    }

    @GetMapping("/sorted/createdDate/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDateState1AndState2() {
        return postService.getAllPostsSortedByCreatedDateForState1AndState2();
    }

    @GetMapping("/answer/{postId}")
    public List<PostResponse.PostAnswerDTO> getPostWithAnswers(@PathVariable(name = "postId") Long postId) {
        return postAnswerService.getAnswersWithPostId(postId);
    }
}
