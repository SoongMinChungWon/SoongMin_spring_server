package com4table.ssupetition.domain.searching.service;

import com4table.ssupetition.domain.searching.config.OpenAiConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QdrantService {

    private final OpenAiConfig domain;

    public List<String> searchSimilar(List<Double> vector) {
        try {
            JSONObject request = new JSONObject()
                    .put("vector", vector)
                    .put("limit", 5);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(domain+"collections/posts/points/search"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(request.toString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JSONArray hits = new JSONObject(response.body()).getJSONArray("result");

            List<String> ids = new ArrayList<>();
            for (int i = 0; i < hits.length(); i++) {
                ids.add(hits.getJSONObject(i).getString("id")); // 또는 payload 내부에서 추출
            }

            return ids;

        } catch (Exception e) {
            throw new RuntimeException("Qdrant 검색 실패", e);
        }
    }
}