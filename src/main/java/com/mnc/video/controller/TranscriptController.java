package com.mnc.video.controller;

import com.mnc.video.model.Transcript;
import com.mnc.video.model.VideoClip;
import com.mnc.video.service.CleaningService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TranscriptController {

    private final CleaningService cleaningService;

    public TranscriptController(CleaningService cleaningService) {
        this.cleaningService = cleaningService;
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyze(@RequestBody Transcript transcript) {
        List<VideoClip> clips = cleaningService.cleanTranscript(transcript.getAllWords());
        String report = cleaningService.generateReport(transcript.getAllWords(), clips);
        
        return Map.of(
            "status", "success",
            "clipCount", clips.size(),
            "report", report,
            "clips", clips
        );
    }
}