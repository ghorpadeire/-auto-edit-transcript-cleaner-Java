package com.mnc.video.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a single word from the transcript.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {
    private String text;
    private double start;
    private double duration;

    // Getters and Setters for Jackson
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public double getStart() { return start; }
    public void setStart(double start) { this.start = start; }
    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public double getEnd() { return start + duration; }
}