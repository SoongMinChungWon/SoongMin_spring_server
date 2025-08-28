package com4table.ssupetition.domain.searching.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QdrantService {

    // ✅ 프로퍼티 키 정정: application.yml에서 app.qdrant.url 로 설정하세요.
    @Value("${app.domain}")
    private String qdrantUrl;

    @Value("${app.qdrant.collection}")
    private String collectionName;

    // 임베딩 모델(text-embedding-3-small) = 1536차원, 거리 Cosine 권장
    @Value("${app.qdrant.vectorSize:1536}")
    private int vectorSize;

    @Value("${app.qdrant.distance:Cosine}")
    private String distance;

    // 재사용 가능한 HttpClient (타임아웃 설정)
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    @PostConstruct
    public void init() {
        ensureCollection(); // 서버 기동 시 1회 컬렉션 보장
    }

    /** 컬렉션이 없으면 생성 (size/distance 맞춤) */
    public void ensureCollection() {
        try {
            var get = HttpRequest.newBuilder()
                    .uri(URI.create(qdrantUrl + "/collections/" + collectionName))
                    .timeout(Duration.ofSeconds(10))
                    .GET().build();

            var resp = client.send(get, HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() == 200) {
                log.info("[QDRANT] collection '{}' exists.", collectionName);
                return;
            }
            log.warn("[QDRANT] collection '{}' not found (status={}). Creating… body={}",
                    collectionName, resp.statusCode(), resp.body());

            JSONObject body = new JSONObject()
                    .put("vectors", new JSONObject()
                            .put("size", vectorSize)
                            .put("distance", distance));

            var put = HttpRequest.newBuilder()
                    .uri(URI.create(qdrantUrl + "/collections/" + collectionName))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            var created = client.send(put, HttpResponse.BodyHandlers.ofString());
            log.info("[QDRANT] create-collection status={} body={}", created.statusCode(), created.body());
            if (created.statusCode() != 200) {
                throw new RuntimeException("Qdrant 컬렉션 생성 실패: " + created.statusCode() + " " + created.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("ensureCollection 실패", e);
        }
    }

    /** 벡터 업서트 (payload는 필요시 주석 해제하여 사용) */
    public void upsertPoint(Long postId, List<Double> vector) {
        try {
            JSONObject point = new JSONObject()
                    .put("id", postId)
                    .put("vector", new JSONArray(vector));
            // .put("payload", new JSONObject()
            //         .put("postId", postId)
            //         .put("createdAt", System.currentTimeMillis() / 1000));

            JSONObject requestBody = new JSONObject()
                    .put("points", new JSONArray().put(point));

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(qdrantUrl + "/collections/" + collectionName + "/points?wait=true"))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.info("[QDRANT] upsert status={} body={}", response.statusCode(), response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Qdrant HTTP " + response.statusCode() + " body=" + response.body());
            }
            JSONObject respJson = new JSONObject(response.body());
            if (!"ok".equalsIgnoreCase(respJson.optString("status"))) {
                throw new RuntimeException("Qdrant upsert 실패: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Qdrant upsertPoint 실패", e);
        }
    }

    /** 유사 검색: 상위 N개의 id 반환 (기본 5개) */
    public List<String> searchSimilar(List<Double> vector) {
        return searchSimilar(vector, 5);
    }

    public List<String> searchSimilar(List<Double> vector, int limit) {
        try {
            JSONObject request = new JSONObject()
                    .put("vector", new JSONArray(vector))
                    .put("limit", limit)
                    .put("with_payload", false)
                    .put("with_vector", false);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(qdrantUrl + "/collections/" + collectionName + "/points/search"))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(request.toString()))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.info("[QDRANT] search status={} body={}", response.statusCode(), response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Qdrant HTTP " + response.statusCode() + " body=" + response.body());
            }

            JSONObject body = new JSONObject(response.body());
            if (!body.has("result")) {
                throw new RuntimeException("Qdrant 응답에 result 없음: " + response.body());
            }

            JSONArray hits = body.getJSONArray("result");
            List<String> ids = new ArrayList<>(hits.length());
            for (int i = 0; i < hits.length(); i++) {
                ids.add(hits.getJSONObject(i).get("id").toString());
            }
            return ids;
        } catch (Exception e) {
            throw new RuntimeException("Qdrant 검색 실패", e);
        }
    }

    /** 포인트 삭제(게시물 삭제 시 동기화) */
    public void deletePoint(Long postId) {
        try {
            JSONObject req = new JSONObject().put("points", new JSONArray().put(postId));
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(qdrantUrl + "/collections/" + collectionName + "/points/delete"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(req.toString()))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            log.info("[QDRANT] delete status={} body={}", response.statusCode(), response.body());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Qdrant HTTP " + response.statusCode() + " body=" + response.body());
            }
            JSONObject respJson = new JSONObject(response.body());
            if (!"ok".equalsIgnoreCase(respJson.optString("status"))) {
                throw new RuntimeException("Qdrant delete 실패: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Qdrant deletePoint 실패", e);
        }
    }
}
