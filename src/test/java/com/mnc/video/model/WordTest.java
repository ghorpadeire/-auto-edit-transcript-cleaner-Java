package com.mnc.video.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void deserializesWordAndEndAliases() throws Exception {
        Word word = mapper.readValue(
                "{\"word\":\"Hello\",\"start\":1.25,\"end\":2.75}",
                Word.class
        );

        assertEquals("Hello", word.getText());
        assertEquals(1.25, word.getStart());
        assertEquals(1.5, word.getDuration());
        assertEquals(2.75, word.getEnd());
    }

    @Test
    void keepsExistingTextAndDurationShape() throws Exception {
        Word word = mapper.readValue(
                "{\"text\":\"Hello\",\"start\":1.25,\"duration\":1.5}",
                Word.class
        );

        assertEquals("Hello", word.getText());
        assertEquals(1.25, word.getStart());
        assertEquals(1.5, word.getDuration());
        assertEquals(2.75, word.getEnd());
    }
}
