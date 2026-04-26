package com.mnc.video.io;

import com.mnc.video.model.VideoClip;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Exports VideoClips as a CMX 3600 EDL file.
 * EDL is the most reliable interchange format for DaVinci Resolve, since
 * Resolve has a known bug ignoring in/out points in FCP7 XMEML imports.
 *
 * Workflow in Resolve:
 *   1. Drag the source .mp4 into the Media Pool first.
 *   2. File -> Import -> Timeline -> Pre-Conformed EDL/XML/AAF -> pick this .edl
 *   3. Resolve relinks clips to the media pool entry by clip name.
 */
public class EdlExporter {

    private final int frameRate = 30;
    // Record TC starts at 01:00:00:00 (broadcast convention, 1 hour = 108000 frames at 30 fps)
    private final long recordStartFrame = 30L * 60L * 60L;

    public void exportToEdl(List<VideoClip> clips, String videoFilePath, String outputEdlPath) throws IOException {
        java.io.File vFile = new java.io.File(videoFilePath);
        String fileName = vFile.getName();
        // EDL "reel" name: 8 chars max, alphanumeric. Use a simple identifier.
        String reel = "AX";

        try (PrintWriter w = new PrintWriter(new FileWriter(outputEdlPath))) {
            w.println("TITLE: Cleaned Sequence");
            w.println("FCM: NON-DROP FRAME");
            w.println();

            long timelineFrame = 0;
            int eventNum = 1;

            for (VideoClip clip : clips) {
                long inFrame  = Math.round(clip.getSourceStart() * frameRate);
                long outFrame = Math.round(clip.getSourceEnd()   * frameRate);
                long duration = outFrame - inFrame;
                if (duration <= 0) continue;

                String srcIn  = framesToTimecode(inFrame);
                String srcOut = framesToTimecode(outFrame);
                String recIn  = framesToTimecode(timelineFrame + recordStartFrame);
                String recOut = framesToTimecode(timelineFrame + duration + recordStartFrame);

                // Event line: NNN REEL     CHAN  TRANS    SRC_IN SRC_OUT REC_IN REC_OUT
                // Channel B = both video and audio. Use V for video-only.
                w.printf("%03d  %-8s %-5s %-8s %s %s %s %s%n",
                    eventNum, reel, "V", "C", srcIn, srcOut, recIn, recOut);
                w.println("* FROM CLIP NAME: " + fileName);
                w.println();

                // Audio event mirroring the video (channel AA = stereo audio pair)
                w.printf("%03d  %-8s %-5s %-8s %s %s %s %s%n",
                    eventNum + 1, reel, "AA", "C", srcIn, srcOut, recIn, recOut);
                w.println("* FROM CLIP NAME: " + fileName);
                w.println();

                timelineFrame += duration;
                eventNum += 2;
            }
        }
    }

    /** Convert frame count to HH:MM:SS:FF timecode (non-drop frame). */
    private String framesToTimecode(long frames) {
        long fps = frameRate;
        long totalSec = frames / fps;
        long ff = frames % fps;
        long ss = totalSec % 60;
        long mm = (totalSec / 60) % 60;
        long hh = (totalSec / 3600);
        return String.format("%02d:%02d:%02d:%02d", hh, mm, ss, ff);
    }
}
