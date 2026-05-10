package com.mnc.video.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single word from the transcript.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {
    @JsonAlias("word")
    private String text;
    private double start;
    private Double duration;
    private Double end;

    // Getters and Setters for Jackson
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public double getStart() { return start; }
    public void setStart(double start) { this.start = start; }
    public double getDuration() {
        if (duration != null) {
            return duration;
        }
        if (end != null) {
            return Math.max(0, end - start);
        }
        return 0;
    }
    public void setDuration(double duration) { this.duration = duration; }
    public void setEnd(double end) { this.end = end; }

    public double getEnd() {
        if (end != null) {
            return end;
        }
        return start + getDuration();
    }
}