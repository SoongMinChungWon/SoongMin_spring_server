package com4table.ssupetition.domain.searching.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.embedding.model}")
    private String embeddingModel;

    @Configuration
    public class OpenAIClientConfig {

        @Bean
        public WebClient openAiWebClient(@Value("${openai.api.key}") String apiKey) {
            return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        }
    }
}