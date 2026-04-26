package com.mnc.video.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

/**
 * Root object for the JSON transcript.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transcript {
    private List<Segment> segments;

    public List<Segment> getSegments() { return segments; }
    public void setSegments(List<Segment> segments) { this.segments = segments; }

    /**
     * Helper to get all words across all segments in a flat list.
     */
    public List<Word> getAllWords() {
        List<Word> allWords = new ArrayList<>();
        if (segments != null) {
            for (Segment s : segments) {
                if (s.getWords() != null) {
                    allWords.addAll(s.getWords());
                }
            }
        }
        return allWords;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Segment {
        private List<Word> words;
        public List<Word> getWords() { return words; }
        public void setWords(List<Word> words) { this.words = words; }
    }
}