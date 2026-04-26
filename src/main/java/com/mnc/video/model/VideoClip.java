package com.mnc.video.model;

/**
 * Represents a segment of video we want to KEEP in the final edit.
 */
public class VideoClip {
    private final double sourceStart;
    private final double sourceEnd;

    public VideoClip(double sourceStart, double sourceEnd) {
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
    }

    public double getSourceStart() { return sourceStart; }
    public double getSourceEnd() { return sourceEnd; }
    public double getDuration() { return sourceEnd - sourceStart; }
}