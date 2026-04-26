package com.mnc.video.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mnc.video.model.Transcript;
import java.io.File;
import java.io.IOException;

/**
 * Handles reading the JSON transcript file.
 */
public class JsonReader {
    private final ObjectMapper mapper = new ObjectMapper();

    public Transcript readFile(String filePath) throws IOException {
        return mapper.readValue(new File(filePath), Transcript.class);
    }
}