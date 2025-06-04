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
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    private final OpenAiConfig openAiConfig;

    public List<Double> getEmbedding(String text) {
        try {
            String cleaned = text.replaceAll("[^가-힣a-zA-Z0-9\\s]", "").toLowerCase();
            String body = String.format("""
                    {
                    "model": "%s",
                    "input": "%s"
                    }
                    """, openAiConfig.getEmbeddingModel(), cleaned);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/embeddings"))
                    .header("Authorization", "Bearer " + openAiConfig.getApiKey())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            JSONArray array = json.getJSONArray("data").getJSONObject(0).getJSONArray("embedding");

            List<Double> result = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                result.add(array.getDouble(i));
            }
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Embedding 요청 실패", e);
        }
    }
}