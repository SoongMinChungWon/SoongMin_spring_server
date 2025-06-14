package com4table.ssupetition.domain.post.controller;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostRequest;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.dto.ResponseDto;
import com4table.ssupetition.domain.post.service.PostAnswerService;
import com4table.ssupetition.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name ="게시글 관련 API", description = "게시글 관련 API에 대한 설명입니다."
    + "추가로 이때 Category는 facility, event, partnership, study, report 중에 하나 넣으면 됨"
    + "type은 state1(투기장 전), state2(투기장 후), state3(답변대기), state4(답변완료) 로 각각 순서대로 매칭해서 생각해서 넣어주면 됨")



public class PostController {

    private final PostService postService;
    private final PostAnswerService postAnswerService;

    @Operation(description = "게시글 작성하는 API")
    @PostMapping("/{userId}")
    public ResponseEntity<Post> addPost(@RequestBody PostRequest.AddDTO addDTO, @PathVariable(name = "userId") Long userId) {
        Post createdPost = postService.addPost(userId, addDTO);
        return ResponseEntity.ok(createdPost);
    }

    //전체 검색
    @Operation(description = "전체 게시글들 가져오는 API")
    @PostMapping("/search")
    public List<PostResponse.AllListDTO> postSearch(@RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        return postService.searchPosts(keyword);
    }


    @Operation(description = "위의 설명을 보면 존재하는 카테고리에 속하는 게시글들을 제공하는 API")
    @PostMapping("/search/sorted-by-agree/{category}")
    public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByAgree(
            @PathVariable(name="category") String category,
            @RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByAgree(category, keyword);
        return ResponseEntity.ok(posts);
    }

    // @Operation(description = "")
    // @PostMapping("/search/sorted-by-expiry/{category}")
    // public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByExpiry(
    //         @PathVariable(name="category") String category,
    //         @RequestBody Map<String, String> body) {
    //     String keyword = body.get("keyword");
    //     List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByExpiry(category, keyword);
    //     return ResponseEntity.ok(posts);
    // }
    //
    // @GetMapping("/search/sorted-by-created-date/{category}")
    // public ResponseEntity<List<PostResponse.AllListDTO>> searchPostsSortedByCreatedDate(
    //         @PathVariable(name="category") String category,
    //         @RequestBody Map<String, String> body) {
    //     String keyword = body.get("keyword");
    //     List<PostResponse.AllListDTO> posts = postService.searchPostsSortedByCreatedDate(category, keyword);
    //     return ResponseEntity.ok(posts);
    // }

    @Operation(description = "AI로 유사한 글을 가져오는 건가 봄")
    @PostMapping("/ai")
    public List<PostResponse.PostAIDTO> postSimilarity(@RequestBody PostRequest.AddDTO addDTO) {
        List<PostResponse.PostAIDTO> similarPost = postService.getSimilarPost(addDTO);
        return similarPost;
    }

    @Operation(description = "postId와 userId를 입력받아서 해당 게시글을 삭제하는 API임.")
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<Void> removePost(@PathVariable(name="postId") Long postId, @PathVariable(name="userId")Long userId) {
        postService.removePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "postId와 userId를 path value로 입력받아서 동의하기")
    @PostMapping("/{postId}/agree/{userId}")
    public ResponseEntity<ResponseDto> addPostAgree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(postService.addPostAgree(postId,userId));
    }

    @Operation(description = "postId와 userId를 path value로 입력받아서 비동의하기")
    @PostMapping("/{postId}/disagree/{userId}")
    public ResponseEntity<ResponseDto> addPostDisagree(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(postService.addPostDisagree(postId,userId));
    }

    @Operation(description = "모든 게시글 가져오기")
    @GetMapping
    public List<PostResponse.AllListDTO> getPostList() {
        return postService.getAllPosts();
    }

    @Operation(description = "모든 동의된 게시글 가져오기")
    @GetMapping("/sorted/agree")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree() {
        return postService.getAllPostsSortedByAgree();
    }


    @Operation(description = "type값에 일치하는 것들 중 동의하는 것을 제공하는 API")
    @GetMapping("/sorted/agree/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgreeExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByAgreeExceptCategory(type);
    }

    @Operation(description = "category와 type값을 입력받은 것들 중 동의하는 것을 제공하는 API")
    @GetMapping("/sorted/agree/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgree(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByAgree(category, type);
    }

    @Operation(description = "동의하는 게시글 중 투기장 전,후 에 대한 것을 제공하는 API")
    @GetMapping("/sorted/agree/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByAgreeState1AndState2() {
        return postService.getAllPostsSortedByAgreeForState1AndState2();
    }


    @Operation(description = "만료일에 따라 정렬해서 제공하는 API")
    @GetMapping("/sorted/expiry")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry() {
        return postService.getAllPostsSortedByExpiry();
    }

    @Operation(description = " type 값에 일치하는 것들을 만료일에 따른 정렬해서 제공하는 API ")
    @GetMapping("/sorted/expiry/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiryExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByExpiryExceptCategory( type);
    }

    @Operation(description = " category와 type 값에 일치하는 것들을 만료일에 따른 정렬해서 제공하는 API ")
    @GetMapping("/sorted/expiry/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiry(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByExpiry(category, type);
    }

    @Operation(description = "만료일에 따라 정렬해서 제공하는 API")
    @GetMapping("/sorted/expiry/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByExpiryState1AndState2() {
        return postService.getAllPostsSortedByExpiryForState1AndState2();
    }

    @Operation(description = "생성일에 따라 정렬해서 제공하는 API")
    @GetMapping("/sorted/createdDate")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate() {
        return postService.getAllPostsSortedByCreatedDate();
    }

    @Operation(description = "type을 입력받아서 생성일에 따라 정렬해서 제공하는 API")
    @GetMapping("/sorted/createdDate/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDateExceptCategory(@PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByCreatedDateExceptCategory(type);
    }

    @Operation(description = " category와 type 값에 일치하는 것들을 생성일에 따른 정렬해서 제공하는 API ")
    @GetMapping("/sorted/createdDate/{category}/{type}")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDate(@PathVariable(name = "category") String category, @PathVariable(name = "type") String type) {
        return postService.getAllPostsSortedByCreatedDate(category, type);
    }

    @Operation(description = "생성일에 따른 정렬해서 투기장 전 후의 게시글을 제공")
    @GetMapping("/sorted/createdDate/state12")
    public List<PostResponse.AllListDTO> getAllPostsSortedByCreatedDateState1AndState2() {
        return postService.getAllPostsSortedByCreatedDateForState1AndState2();
    }


    @Operation(description = "postId를 입력받아서 담당자의 답변을 가져오는 API")
    @GetMapping("/answer/{postId}")
    public List<PostResponse.PostAnswerDTO> getPostWithAnswers(@PathVariable(name = "postId") Long postId) {
        return postAnswerService.getAnswersWithPostId(postId);
    }
}
