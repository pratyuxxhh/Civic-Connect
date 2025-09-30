package com.example.civic_issues.services.aiService;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AiService

{

    public boolean validate(String base64Image, String description, String department) {
        Client client = Client.builder().apiKey("AIzaSyAHac9_5D-nxkMgzuG6Kq8--5IEFYqWphY").build();
        String prompt = "You are a civic issue validator. Given an image and a description," +
                " check if the image matches the description and determine if it represents a civic issue " +
                "(like potholes, garbage, broken lights, road damage, waterlogging,bad sewage condition, etc.). " +
                "Return only '1' if it matches and is a civic issue, else return '0'. " +
                "Description: " + description;
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            prompt,
                            null);
            log.info(response.text());
        return Integer.parseInt(Objects.requireNonNull(response.text())) == 1;
    }
}
