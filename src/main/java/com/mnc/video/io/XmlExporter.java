package com.mnc.video.io;

import com.mnc.video.model.VideoClip;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class XmlExporter {

    private final int frameRate = 30;
    private final int width = 1920;
    private final int height = 1080;

    public void exportToXml(List<VideoClip> clips, String videoFilePath, String outputXmlPath) throws IOException {
        java.io.File vFile = new java.io.File(videoFilePath);
        String fileName = vFile.getName();
        String absolutePath = vFile.getAbsolutePath().replace("\\", "/");
        String encodedPath = encodeUrl(absolutePath);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputXmlPath))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<!DOCTYPE xmeml>");
            writer.println("<xmeml version=\"5\">");
            writer.println("  <sequence id=\"sequence-1\">");
            writer.println("    <name>Cleaned Sequence</name>");
            writer.println("    <rate><timebase>" + frameRate + "</timebase><ntsc>FALSE</ntsc></rate>");
            writer.println("    <media>");
            writer.println("      <video>");
            writer.println("        <format>");
            writer.println("          <samplecharacteristics>");
            writer.println("            <width>" + width + "</width>");
            writer.println("            <height>" + height + "</height>");
            writer.println("          </samplecharacteristics>");
            writer.println("        </format>");
            writer.println("        <track>");

            // Generate Video Clips
            writeClips(writer, clips, fileName, encodedPath, "video");

            writer.println("        </track>");
            writer.println("      </video>");
            
            // Add Audio Tracks (Required for Sync and Import success)
            writer.println("      <audio>");
            writer.println("        <track>");
            writeClips(writer, clips, fileName, encodedPath, "audio");
            writer.println("        </track>");
            writer.println("        <track>");
            writeClips(writer, clips, fileName, encodedPath, "audio");
            writer.println("        </track>");
            writer.println("      </audio>");
            
            writer.println("    </media>");
            writer.println("  </sequence>");
            writer.println("</xmeml>");
        }
    }

    private void writeClips(PrintWriter writer, List<VideoClip> clips, String fileName, String path, String type) {
        long currentTimelineFrame = 0;
        for (VideoClip clip : clips) {
            long startFrame = Math.round(clip.getSourceStart() * frameRate);
            long endFrame = Math.round(clip.getSourceEnd() * frameRate);
            long durationFrames = endFrame - startFrame;

            if (durationFrames <= 0) continue;

            writer.println("          <clipitem id=\"" + type + "-" + currentTimelineFrame + "\">");
            writer.println("            <name>" + fileName + "</name>");
            writer.println("            <duration>" + (100 * frameRate * 60) + "</duration>"); // Placeholder large duration
            writer.println("            <rate><timebase>" + frameRate + "</timebase></rate>");
            writer.println("            <start>" + currentTimelineFrame + "</start>");
            writer.println("            <end>" + (currentTimelineFrame + durationFrames) + "</end>");
            writer.println("            <in>" + startFrame + "</in>");
            writer.println("            <out>" + endFrame + "</out>");
            writer.println("            <file id=\"file-1\">");
            writer.println("              <name>" + fileName + "</name>");
            writer.println("              <pathurl>file://localhost/" + path + "</pathurl>");
            writer.println("            </file>");
            if (type.equals("audio")) {
                writer.println("            <sourcetrack><mediatype>audio</mediatype><trackindex>1</trackindex></sourcetrack>");
            }
            writer.println("          </clipitem>");

            currentTimelineFrame += durationFrames;
        }
    }

    public String generateXmlString(List<VideoClip> clips, String videoFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE xmeml>\n");
        sb.append("<xmeml version=\"5\">\n");
        sb.append("  <sequence id=\"sequence-1\">\n");
        sb.append("    <name>Cleaned Sequence</name>\n");
        sb.append("    <rate><timebase>").append(frameRate).append("</timebase><ntsc>FALSE</ntsc></rate>\n");
        sb.append("    <media>\n");
        sb.append("      <video>\n");
        sb.append("        <format>\n");
        sb.append("          <samplecharacteristics>\n");
        sb.append("            <width>").append(width).append("</width>\n");
        sb.append("            <height>").append(height).append("</height>\n");
        sb.append("          </samplecharacteristics>\n");
        sb.append("        </format>\n");
        sb.append("        <track>\n");

        long currentTimelineFrame = 0;
        for (VideoClip clip : clips) {
            long startFrame = Math.round(clip.getSourceStart() * frameRate);
            long endFrame = Math.round(clip.getSourceEnd() * frameRate);
            long durationFrames = endFrame - startFrame;
            if (durationFrames <= 0) continue;

            sb.append("          <clipitem id=\"v-").append(currentTimelineFrame).append("\">\n");
            sb.append("            <name>").append(videoFileName).append("</name>\n");
            sb.append("            <rate><timebase>").append(frameRate).append("</timebase></rate>\n");
            sb.append("            <start>").append(currentTimelineFrame).append("</start>\n");
            sb.append("            <end>").append(currentTimelineFrame + durationFrames).append("</end>\n");
            sb.append("            <in>").append(startFrame).append("</in>\n");
            sb.append("            <out>").append(endFrame).append("</out>\n");
            sb.append("            <file id=\"file-1\"><name>").append(videoFileName).append("</name></file>\n");
            sb.append("          </clipitem>\n");
            currentTimelineFrame += durationFrames;
        }

        sb.append("        </track>\n");
        sb.append("      </video>\n");
        sb.append("    </media>\n");
        sb.append("  </sequence>\n");
        sb.append("</xmeml>");
        return sb.toString();
    }

    private String encodeUrl(String path) {
        return path.replace(" ", "%20")
                   .replace("(", "%28")
                   .replace(")", "%29");
    }
}