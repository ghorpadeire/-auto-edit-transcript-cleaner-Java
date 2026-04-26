package com.mnc.video.logic;

import com.mnc.video.filter.TranscriptFilter;
import com.mnc.video.model.VideoClip;
import com.mnc.video.model.Word;
import java.util.ArrayList;
import java.util.List;

public class TranscriptProcessor {
    private final List<TranscriptFilter> filters = new ArrayList<>();
    private final double pauseThreshold;
    private final double padding;

    public TranscriptProcessor(double pauseThreshold, double padding) {
        this.pauseThreshold = pauseThreshold;
        this.padding = padding;
    }

    public void addFilter(TranscriptFilter filter) {
        filters.add(filter);
    }

    public List<VideoClip> generateClips(List<Word> words) {
        List<VideoClip> clips = new ArrayList<>();
        if (words.isEmpty()) return clips;

        double currentClipStart = -1;
        Word previousWord = null;

        for (int i = 0; i < words.size(); i++) {
            Word current = words.get(i);
            
            boolean keep = true;
            for (TranscriptFilter filter : filters) {
                if (!filter.evaluate(current, i, previousWord)) {
                    keep = false;
                    break;
                }
            }

            if (keep) {
                if (currentClipStart == -1) {
                    currentClipStart = Math.max(0, current.getStart() - padding);
                } else if (previousWord != null) {
                    double gap = current.getStart() - previousWord.getEnd();
                    if (gap > pauseThreshold) {
                        clips.add(new VideoClip(currentClipStart, previousWord.getEnd() + padding));
                        currentClipStart = Math.max(0, current.getStart() - padding);
                    }
                }
                previousWord = current;
            } else {
                if (previousWord == null) {
                    currentClipStart = -1; 
                }
            }
        }

        if (currentClipStart != -1 && previousWord != null) {
            clips.add(new VideoClip(currentClipStart, previousWord.getEnd() + padding));
        }

        return clips;
    }
}