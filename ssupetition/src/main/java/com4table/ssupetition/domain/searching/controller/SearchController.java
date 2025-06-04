package com4table.ssupetition.domain.searching.controller;

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

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final EmbeddingService embeddingService;
    private final QdrantService qdrantService;

    @PostMapping
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request) {
        List<Double> embedding = embeddingService.getEmbedding(request.getQuery());
        List<String> ids = qdrantService.searchSimilar(embedding);
        return ResponseEntity.ok(new SearchResult(ids));
    }
}