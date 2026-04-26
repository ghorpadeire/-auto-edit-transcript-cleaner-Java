package com.mnc.video.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HomeController {

    // Removed the "/" mapping so that index.html can be served automatically
    @GetMapping("/api/status")
    public Map<String, String> status() {
        return Map.of(
            "status", "online",
            "version", "1.0.4"
        );
    }
}