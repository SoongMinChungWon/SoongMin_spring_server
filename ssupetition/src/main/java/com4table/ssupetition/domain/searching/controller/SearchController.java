package com4table.ssupetition.domain.searching.controller;

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

import java.util.List;
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

        // 3) 문자열 id → Long으로 변환
        List<Long> ids = idStrings.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 4) JPA를 통해 MySQL에서 해당 id들을 한꺼번에 조회
        List<PostResponse.AllListDTO> posts = postRepository
                .findAllById(ids)   // JpaRepository가 상속해서 제공해줍니다.
                .stream()
                // 5) 엔티티(Post)를 “AllListDTO”로 변환
                .map(PostResponse.AllListDTO::new)
                .collect(Collectors.toList());

        // 6) 최종 SearchResult에 담아서 반환
        return ResponseEntity.ok(new SearchResult(posts));
    }
}