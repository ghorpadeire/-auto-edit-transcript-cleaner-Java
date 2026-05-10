package com.mnc.video.filter;

import com.mnc.video.model.Word;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class TranscriptFilterTest {

    @Test
    void fillerFilterRejectsMissingTextWithoutThrowing() {
        Word word = new Word();

        assertFalse(new FillerWordFilter().evaluate(word, 0, null));
    }

    @Test
    void actionFilterRejectsMissingTextWithoutThrowing() {
        Word word = new Word();

        assertFalse(new ActionWordFilter().evaluate(word, 0, null));
    }
}
