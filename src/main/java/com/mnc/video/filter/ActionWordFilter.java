package com.mnc.video.filter;

import com.mnc.video.model.Word;
import java.util.Arrays;
import java.util.List;

/**
 * Filter to remove "Action words" if they occur at the very beginning.
 */
public class ActionWordFilter implements TranscriptFilter {
    private final List<String> blacklist = Arrays.asList("action", "start", "okay", "now");

    @Override
    public boolean evaluate(Word currentWord, int index, Word previousWord) {
        // If it's the first word and in our blacklist, remove it
        if (index == 0 && blacklist.contains(currentWord.getText().toLowerCase().replaceAll("[^a-zA-Z]", ""))) {
            return false;
        }
        return true;
    }
}