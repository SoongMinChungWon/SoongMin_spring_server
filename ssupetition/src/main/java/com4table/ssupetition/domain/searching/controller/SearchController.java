package com4table.ssupetition.domain.searching.controller;

import com4table.ssupetition.domain.post.domain.Post;
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
        // 1) 클라이언트로부터 받은 쿼리문 → 임베딩 벡터 생성
        List<Double> embedding = embeddingService.getEmbedding(request.getQuery());
        // 2) Qdrant에 벡터 검색 요청 → 유사도 상위 5개(기본) post_id 문자열 리스트 반환
        List<String> idStrings = qdrantService.searchSimilar(embedding);
        // 3) 문자열 ID를 Long으로 변환
        List<Long> ids = idStrings.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 4) MySQL에서 해당 ID 목록을 한 번에 가져옴
        //    findAllById(List<Long>)는 JpaRepository에 기본 제공됩니다.
        List<Post> posts = postRepository.findAllById(ids);

        // 5) Qdrant가 반환한 순서대로 결과를 정렬하기 위해 Map으로 정리
        //    (MySQL findAllById(...)는 IN 절 내부 순서와 관계없이 임의 순서로 리턴되기 때문입니다)
        //    map.keySet() == DB에 있는 ID 목록
        var mapById = posts.stream()
                .collect(Collectors.toMap(Post::getPostId, post -> post));

        // 6) idStrings 순서대로 실제 Post 객체를 꺼내서 최종 리스트를 만든다.
        //    “DB에 없는 ID(맵에 없는 ID)가 섞여 있을 경우”는 null 체크 후 무시하도록 합니다.
        List<Post> orderedPosts = ids.stream()
                .map(mapById::get)
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // 7) 실질적으로 돌려줄 SearchResult(여기서는 Post 엔티티 리스트)를 생성
        SearchResult result = new SearchResult(orderedPosts);

        return ResponseEntity.ok(result);
    }
}