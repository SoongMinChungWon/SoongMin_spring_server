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

    private static final String QDRANT_URL = "http://52.78.204.183:6333"; // EC2에서 Spring Boot를 직접 실행 중이라면 localhost 사용
    private static final String COLLECTION_NAME = "posts";            // 사용할 컬렉션 이름


    public List<String> searchSimilar(List<Double> vector) {
        try {
            JSONObject request = new JSONObject()
                    .put("vector", vector)
                    .put("limit", 5);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://52.78.204.183:6333/collections/posts/points/search"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(request.toString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject body = new JSONObject(response.body());

            if (!body.has("result")) {
                throw new RuntimeException("Qdrant 응답에 result 없음: " + response.body());
            }

            JSONArray hits = body.getJSONArray("result");

            List<String> ids = new ArrayList<>();
            for (int i = 0; i < hits.length(); i++) {
                String idString = hits.getJSONObject(i).get("id").toString();
                ids.add(idString);
            }

            return ids;

        } catch (Exception e) {
            throw new RuntimeException("Qdrant 검색 실패", e);
        }
    }

    public void upsertPoint(Long postId, List<Double> vector) {
        try {
            // Qdrant API에 요구하는 JSON 형식: {"points":[{"id":..., "vector":[...]}]}
            JSONObject point = new JSONObject()
                    .put("id", postId)        // Qdrant에 저장할 ID (Long)
                    .put("vector", new JSONArray(vector)); // 벡터 데이터 (List<Double>)

            JSONObject requestBody = new JSONObject()
                    .put("points", new JSONArray().put(point));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(QDRANT_URL + "/collections/" + COLLECTION_NAME + "/points?wait=true"))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JSONObject respJson = new JSONObject(response.body());
            if (!"ok".equalsIgnoreCase(respJson.optString("status"))) {
                throw new RuntimeException("Qdrant upsert 실패: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Qdrant upsertPoint 실패", e);
        }
    }
}