package com.example.server.controller;

import com.example.server.service.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ClassifierController {

    @Autowired
    private HuggingFaceService huggingFaceService;

    // This creates the POST endpoint at http://localhost:8080/api/classify
    @PostMapping("/classify")
    public Mono<ResponseEntity<String>> classifyImage(@RequestParam("file") MultipartFile file) {
        try {
            // Get the bytes from the uploaded file
            byte[] imageBytes = file.getBytes();
            // Call the service to get the prediction
            return huggingFaceService.classifyImage(imageBytes)
                    .map(result -> ResponseEntity.ok(result)) // If successful, return 200 OK with the result
                    .defaultIfEmpty(ResponseEntity.status(500).build()); // Handle empty response
        } catch (IOException e) {
            e.printStackTrace();
            // If there's an error reading the file, return an error response
            return Mono.just(ResponseEntity.status(500).body("Error processing file."));
        }
    }
}