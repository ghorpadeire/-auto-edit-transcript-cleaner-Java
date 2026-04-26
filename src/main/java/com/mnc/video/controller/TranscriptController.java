package com.mnc.video.controller;

import com.mnc.video.model.Transcript;
import com.mnc.video.model.VideoClip;
import com.mnc.video.service.CleaningService;
import com.mnc.video.io.XmlExporter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TranscriptController {

    private final CleaningService cleaningService;
    private final XmlExporter xmlExporter = new XmlExporter();

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

    @PostMapping("/analyze/download")
    public Map<String, Object> analyzeAndDownload(@RequestBody Transcript transcript) {
        List<VideoClip> clips = cleaningService.cleanTranscript(transcript.getAllWords());
        String report = cleaningService.generateReport(transcript.getAllWords(), clips);
        String xml = xmlExporter.generateXmlString(clips, "video.mp4");
        
        return Map.of(
            "status", "success",
            "report", report,
            "xml", xml
        );
    }
}