package com.mnc.video.io;

import com.mnc.video.model.VideoClip;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Generates an FFmpeg command script to verify the cuts.
 */
public class FfmpegExporter {

    public void exportPreviewScript(List<VideoClip> clips, String videoFilePath, String outputPath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.print("ffmpeg -y ");
            
            // Build the filter_complex string
            StringBuilder filter = new StringBuilder("-filter_complex \"");
            for (int i = 0; i < clips.size(); i++) {
                VideoClip clip = clips.get(i);
                writer.printf("-ss %.3f -t %.3f -i \"%s\" ", 
                    clip.getSourceStart(), clip.getDuration(), videoFilePath);
            }
            
            // Stitch them together
            for (int i = 0; i < clips.size(); i++) {
                filter.append("[").append(i).append(":v][").append(i).append(":a]");
            }
            filter.append("concat=n=").append(clips.size()).append(":v=1:a=1[outv][outa]\" ");
            filter.append("-map \"[outv]\" -map \"[outa]\" preview_test.mp4");
            
            writer.println(filter.toString());
        }
    }
}