package com.secure.fastquiz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Value("${ai.openai.api-key}")
    private String apiKey;  // Accede a la propiedad correctamente

    public String getApiKey() {
        return apiKey;
    }
}
