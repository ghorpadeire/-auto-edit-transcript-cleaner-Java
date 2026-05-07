package com.mnc.video.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;

class TranscriptTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void deserializesCommonWordAndEndTranscriptShape() throws Exception {
        Transcript transcript = mapper.readValue("""
            {
              "segments": [
                {
                  "words": [
                    { "word": "Hello", "start": 1.0, "end": 1.4 }
                  ]
                }
              ]
            }
            """, Transcript.class);

        List<Word> words = transcript.getAllWords();

        assertThat(words).hasSize(1);
        assertThat(words.get(0).getText()).isEqualTo("Hello");
        assertThat(words.get(0).getDuration()).isCloseTo(0.4, within(0.001));
        assertThat(words.get(0).getEnd()).isCloseTo(1.4, within(0.001));
    }

    @Test
    void skipsNullSegmentsAndWords() throws Exception {
        Transcript transcript = mapper.readValue("""
            {
              "segments": [
                null,
                { "words": [null, { "text": "kept", "start": 0.0, "duration": 0.5 }] }
              ]
            }
            """, Transcript.class);

        assertThat(transcript.getAllWords())
            .singleElement()
            .extracting(Word::getText)
            .isEqualTo("kept");
    }
}
