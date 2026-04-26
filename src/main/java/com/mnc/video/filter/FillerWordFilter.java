package com.mnc.video.filter;

import com.mnc.video.model.Word;
import java.util.Arrays;
import java.util.List;

/**
 * Filter to remove filler words and empty disfluencies.
 */
public class FillerWordFilter implements TranscriptFilter {
    private final List<String> fillers = Arrays.asList("um", "uh", "ah", "hmm", "err");

    @Override
    public boolean evaluate(Word currentWord, int index, Word previousWord) {
        String text = currentWord.getText().toLowerCase().trim();
        
        // 1. Remove if it's an empty sound (disfluency)
        if (text.isEmpty()) {
            return false;
        }

        // 2. Remove if it's a filler word
        if (fillers.contains(text.replaceAll("[^a-z]", ""))) {
            return false;
        }

        return true;
    }
}