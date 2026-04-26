package com.mnc.video.filter;

import com.mnc.video.model.Word;

/**
 * Interface for any logic that decides if a word should be kept or ignored.
 */
public interface TranscriptFilter {
    /**
     * @param currentWord The word being evaluated.
     * @param index The position in the transcript.
     * @param previousWord The previous word (can be null for the first word).
     * @return true to KEEP, false to REJECT.
     */
    boolean evaluate(Word currentWord, int index, Word previousWord);
}