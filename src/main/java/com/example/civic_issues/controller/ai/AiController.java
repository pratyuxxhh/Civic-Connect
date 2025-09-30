package com.example.civic_issues.controller.ai;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
@RestController
@RequestMapping("/ai")
public class AiController {
    Client client = Client.builder().apiKey("AIzaSyAHac9_5D-nxkMgzuG6Kq8--5IEFYqWphY").build();

    @GetMapping("/health-check")
    public ResponseEntity<String> checkHealth(){
        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "hie there , this is for health check",
                        null);

        return ResponseEntity.ok(response.text());
    }

}
