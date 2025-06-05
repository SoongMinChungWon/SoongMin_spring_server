package com4table.ssupetition.domain.searching.dto;

import com4table.ssupetition.domain.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult {
    // Qdrant가 돌려준 순서대로 정렬된 실제 Post 리스트
    private List<Post> posts;
}