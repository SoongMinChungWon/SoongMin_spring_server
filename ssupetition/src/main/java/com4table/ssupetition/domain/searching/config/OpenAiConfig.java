package com4table.ssupetition.domain.searching.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.embedding.model}")
    private String embeddingModel;

    @Value("${app.domain}")
    private String domain;

}