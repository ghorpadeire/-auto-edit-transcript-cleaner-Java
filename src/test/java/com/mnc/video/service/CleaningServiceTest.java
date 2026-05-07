package com.mnc.video.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.mnc.video.model.Word;
import java.util.List;
import org.junit.jupiter.api.Test;

class CleaningServiceTest {

    private final CleaningService cleaningService = new CleaningService(1.5, 0.2);

    @Test
    void ignoresWordsWithMissingTextInsteadOfThrowing() {
        Word missingText = new Word();
        missingText.setStart(0.0);
        missingText.setDuration(0.3);

        Word spokenWord = new Word();
        spokenWord.setText("hello");
        spokenWord.setStart(0.5);
        spokenWord.setDuration(0.4);

        assertThat(cleaningService.cleanTranscript(List.of(missingText, spokenWord)))
            .hasSize(1)
            .first()
            .satisfies(clip -> {
                assertThat(clip.getSourceStart()).isCloseTo(0.3, within(0.001));
                assertThat(clip.getSourceEnd()).isCloseTo(1.1, within(0.001));
            });
    }
}
