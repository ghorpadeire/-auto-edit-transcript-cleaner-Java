package com.mnc.video.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
            "status", "online",
            "service", "MNC Transcript Cleaner API",
            "version", "1.0.4",
            "message", "The API is running. Use the /api/v1/analyze endpoint for processing."
        );
    }
}