package com.example.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HuggingFaceService {

    // These @Value annotations read the properties from your application.properties file
    @Value("${huggingface.api.key}")
    private String apiKey;

    @Value("${huggingface.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    // Constructor to build the WebClient
    public HuggingFaceService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // This method makes the actual API call to Hugging Face
    public Mono<String> classifyImage(byte[] imageBytes) {
        return webClient.post()
                .uri(apiUrl) // The URL from your properties file
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey) // The API key from your properties file
                .contentType(MediaType.IMAGE_JPEG) // Assuming the image is a JPEG
                .bodyValue(imageBytes) // The actual image data
                .retrieve()
                .bodyToMono(String.class); // Get the response as a JSON string
    }
}