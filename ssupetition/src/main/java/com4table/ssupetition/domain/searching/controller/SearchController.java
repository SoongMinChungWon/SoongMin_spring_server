package com4table.ssupetition.domain.searching.controller;

import com4table.ssupetition.domain.post.domain.Post;
import com4table.ssupetition.domain.post.dto.PostResponse;
import com4table.ssupetition.domain.post.repository.PostRepository;
import com4table.ssupetition.domain.searching.dto.SearchRequest;
import com4table.ssupetition.domain.searching.dto.SearchResult;
import com4table.ssupetition.domain.searching.service.EmbeddingService;
import com4table.ssupetition.domain.searching.service.QdrantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;
    private final PostRepository postRepository;

    @PostMapping
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        // 1) 사용자 쿼리 → OpenAI 임베딩
        List<Double> embedding = embeddingService.getEmbedding(request.getQuery());

        // 2) Qdrant에서 유사도 Top-N 포인트(id 문자열) 조회
        List<String> idStrings = qdrantService.searchSimilar(embedding);

        // 3) 문자열 id → Long으로 변환 (원본 순서 보존)
        List<Long> orderedIds = idStrings.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 4) JPA를 통해 해당 ID들을 한꺼번에 조회 (순서 보장 안 됨)
        List<com4table.ssupetition.domain.post.domain.Post> fetchedPosts =
                postRepository.findAllById(orderedIds);

        // 5) “ID → 엔티티(Post)” 맵 생성
        Map<Long, Post> postMap = fetchedPosts.stream()
                .collect(Collectors.toMap(
                        com4table.ssupetition.domain.post.domain.Post::getPostId,
                        post -> post
                ));

        // 6) 원본 orderedIds 순서대로 꺼내서 DTO로 변환
        List<PostResponse.AllListDTO> orderedDtoList = new ArrayList<>();
        for (Long id : orderedIds) {
            com4table.ssupetition.domain.post.domain.Post p = postMap.get(id);
            if (p != null) {
                orderedDtoList.add(new PostResponse.AllListDTO(p));
            }
            // 만약 p == null 이라면(예: DB에서 이미 삭제된 포스트),
            // 여기서 스킵하거나 별도 로깅할 수 있습니다.
        }

        // 7) 결과를 SearchResult로 감싸서 반환
        return ResponseEntity.ok(new SearchResult(orderedDtoList));
    }
}