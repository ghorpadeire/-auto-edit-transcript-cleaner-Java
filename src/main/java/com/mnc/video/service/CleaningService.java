package com.mnc.video.service;

import com.mnc.video.filter.ActionWordFilter;
import com.mnc.video.filter.FillerWordFilter;
import com.mnc.video.logic.TranscriptProcessor;
import com.mnc.video.model.VideoClip;
import com.mnc.video.model.Word;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CleaningService {

    private final TranscriptProcessor processor;

    public CleaningService(
            @Value("${cleaner.pause-threshold}") double threshold,
            @Value("${cleaner.padding}") double padding) {
        this.processor = new TranscriptProcessor(threshold, padding);
        this.processor.addFilter(new ActionWordFilter());
        this.processor.addFilter(new FillerWordFilter());
    }

    public List<VideoClip> cleanTranscript(List<Word> words) {
        return processor.generateClips(words);
    }

    public String generateReport(List<Word> original, List<VideoClip> clips) {
        double originalDuration = original.isEmpty() ? 0 : original.get(original.size() - 1).getEnd();
        double finalDuration = clips.stream().mapToDouble(VideoClip::getDuration).sum();
        
        return String.format(
            "--- EDIT REPORT ---\n" +
            "Original Words: %d\n" +
            "Original Duration: %.2fs\n" +
            "Final Clips: %d\n" +
            "Final Duration: %.2fs\n" +
            "Reduction: %.2fs\n" +
            "-------------------",
            original.size(), originalDuration, clips.size(), finalDuration, (originalDuration - finalDuration)
        );
    }
}