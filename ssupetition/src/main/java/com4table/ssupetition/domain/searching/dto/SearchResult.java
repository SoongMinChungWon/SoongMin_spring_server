package com4table.ssupetition.domain.searching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResult {
    private List<String> ids;
}