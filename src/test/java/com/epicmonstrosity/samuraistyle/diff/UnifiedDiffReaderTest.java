package com.epicmonstrosity.samuraistyle.diff;

import com.epicmonstrosity.samuraistyle.diff.fragments.DiffHunk;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Deque;

class UnifiedDiffReaderTest {

    @Test
    void readMultipleGitDiff() {
        final String[] lines = {
                "diff --git a/README.md b/README.md",
                "index 6ba0b80..1f6ef0c 100644",
                "--- a/README.md",
                "+++ b/README.md",
                "@@ -24,7 +24,7 @@ The Minimum Supported Rust Version for `pixels` will always be made available in",
                "     - DirectX 11, WebGL2, and WebGPU support are a work in progress.",
                " - Use your own custom shaders for special effects.",
                " - Hardware accelerated scaling on perfect pixel boundaries.",
                "-- Supports non-square pixel aspect ratios. (WIP)",
                "+- Supports non-square pixel aspect ratios.",
                " ",
                " ## Examples",
                " ",
                "@@ -36,6 +36,7 @@ The Minimum Supported Rust Version for `pixels` will always be made available in",
                " - [Minimal example with SDL2](./examples/minimal-sdl2)",
                " - [Minimal example with `winit`](./examples/minimal-winit)",
                " - [Minimal example with `fltk`](./examples/minimal-fltk)",
                "+- [Non-square pixel aspect ratios](./examples/pixel-aspect-ratio)",
                " - [Pixel Invaders](./examples/invaders)",
                " - [`raqote` example](./examples/raqote-winit)",
                " ",
                "diff --git a/img/pixel-aspect-ratio-2.png b/img/pixel-aspect-ratio-2.png",
                "new file mode 100644",
                "index 0000000..fa3f2c0",
                "Binary files /dev/null and b/img/pixel-aspect-ratio-2.png differ",
                "diff --git a/img/pixel-aspect-ratio.png b/img/pixel-aspect-ratio.png",
                "new file mode 100644",
                "index 0000000..666bf09",
                "Binary files /dev/null and b/img/pixel-aspect-ratio.png differ",
                "diff --git a/examples/minimal-fltk/src/main.rs b/examples/minimal-fltk/src/main.rs",
                "index 128ba48..f57c948 100644",
                "--- a/examples/minimal-fltk/src/main.rs",
                "+++ b/examples/minimal-fltk/src/main.rs",
                "@@ -98,12 +98,13 @@ impl World {",
                "         for (i, pixel) in frame.chunks_exact_mut(4).enumerate() {",
                "             let x = (i % WIDTH as usize) as i16;",
                "             let y = (i / WIDTH as usize) as i16;",
                "-            let d = {",
                "-                let xd = x as i32 - self.circle_x as i32;",
                "-                let yd = y as i32 - self.circle_y as i32;",
                "-                ((xd.pow(2) + yd.pow(2)) as f64).sqrt().powi(2)",
                "+            let length = {",
                "+                let x = (x - self.circle_x) as f64;",
                "+                let y = (y - self.circle_y) as f64;",
                "+",
                "+                x.powf(2.0) + y.powf(2.0)",
                "             };",
                "-            let inside_the_circle = d < (CIRCLE_RADIUS as f64).powi(2);",
                "+            let inside_the_circle = length < (CIRCLE_RADIUS as f64).powi(2);",
                " ",
                "             let rgba = if inside_the_circle {",
                "                 [0xac, 0x00, 0xe6, 0xff]"
        };
        final UnifiedDiffReader reader = UnifiedDiffReader.parse(lines);
        Assertions.assertNotNull(reader);
        final Deque<DiffDocument> documents = reader.getDocuments();
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(4, documents.size());

        // First Document
        final DiffDocument first = documents.poll();
        Assertions.assertNotNull(first);
        Assertions.assertNotNull(first.getGitDiffHeader());
        //Assertions.assertNotNull(first.getIndex());
        Assertions.assertEquals("README.md", first.getOldFilename().getFilename());
        Assertions.assertEquals("README.md", first.getNewFilename().getFilename());

        Deque<DiffHunk> hunkList = first.getHunkList();
        Assertions.assertFalse(hunkList.isEmpty());

        DiffHunk firstHunk = hunkList.poll();
        Assertions.assertNotNull(firstHunk);
        Assertions.assertEquals(24, firstHunk.getFromRange().getLineStart());
        Assertions.assertEquals(7, firstHunk.getFromRange().getRangeSize());
        Assertions.assertEquals(24, firstHunk.getToRange().getLineStart());
        Assertions.assertEquals(7, firstHunk.getToRange().getRangeSize());

        final DiffHunk secondHunk = hunkList.poll();
        Assertions.assertNotNull(secondHunk);
        Assertions.assertEquals(36, secondHunk.getFromRange().getLineStart());
        Assertions.assertEquals(6, secondHunk.getFromRange().getRangeSize());
        Assertions.assertEquals(36, secondHunk.getToRange().getLineStart());
        Assertions.assertEquals(7, secondHunk.getToRange().getRangeSize());

        // Second Document
        final DiffDocument second = documents.poll();
        Assertions.assertNotNull(second);
        Assertions.assertNotNull(second.getGitDiffHeader());
        //Assertions.assertNotNull(first.getIndex());
        Assertions.assertNull(second.getOldFilename());
        Assertions.assertNull(second.getNewFilename());
        Assertions.assertTrue(second.getHunkList().isEmpty());

        // Fourth Document
        documents.poll();
        final DiffDocument fourth = documents.poll();
        Assertions.assertNotNull(fourth);
        Assertions.assertNotNull(fourth.getGitDiffHeader());
        //Assertions.assertNotNull(first.getIndex());
        Assertions.assertEquals("examples/minimal-fltk/src/main.rs", fourth.getOldFilename().getFilename());
        Assertions.assertEquals("examples/minimal-fltk/src/main.rs", fourth.getNewFilename().getFilename());

        hunkList = fourth.getHunkList();
        Assertions.assertFalse(hunkList.isEmpty());

        firstHunk = hunkList.poll();
        Assertions.assertNotNull(firstHunk);
        Assertions.assertEquals(98, firstHunk.getFromRange().getLineStart());
        Assertions.assertEquals(12, firstHunk.getFromRange().getRangeSize());
        Assertions.assertEquals(98, firstHunk.getToRange().getLineStart());
        Assertions.assertEquals(13, firstHunk.getToRange().getRangeSize());
    }

    @Test
    void readUnifiedDiffHeader() {
        final String[] lines = {
                "diff --git a/builtin-http-fetch.c b/http-fetch.c",
                "similarity index 95%",
                "rename from builtin-http-fetch.c",
                "rename to http-fetch.c",
                "index f3e63d7..e8f44ba 100644",
                "--- a/builtin-http-fetch.c",
                "+++ b/http-fetch.c"
        };
        final UnifiedDiffReader reader = UnifiedDiffReader.parse(lines);
        Assertions.assertNotNull(reader);

        final Deque<DiffDocument> documents = reader.getDocuments();
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(1, documents.size());

        final DiffDocument first = documents.poll();
        Assertions.assertNotNull(first);
        Assertions.assertNotNull(first.getGitDiffHeader());
        Assertions.assertEquals("builtin-http-fetch.c", first.getOldFilename().getFilename());
        Assertions.assertEquals("http-fetch.c", first.getNewFilename().getFilename());
    }

    @Test
    void readUnifiedDiffLines() {
        final String[] lines = {
                "diff --git a/src/com/example/application/data/service/SomeService.java b/src/com/example/application/data/service/SomeService.java",
                "index f3e63d7..e8f44ba 100644",
                "--- a/src/com/example/application/data/service/SomeService.java",
                "+++ b/src/com/example/application/data/service/SomeService.java",
                "@@ -1,6 +1,4 @@",
                " 1",
                "-2",
                "-3",
                " 4",
                " 5",
                " 6",
                "@@ -11,6 +9,4 @@",
                " 11",
                " 12",
                " 13",
                "-14",
                "-15",
                " 16",
                "@@ -17 +13,4 @@",
                "+17",
                "+18",
                "+19",
                "+20",
        };
        final UnifiedDiffReader reader = UnifiedDiffReader.parse(lines);
        Assertions.assertNotNull(reader);

        final Deque<DiffDocument> documents = reader.getDocuments();
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(1, documents.size());

        final DiffDocument first = documents.poll();
        Assertions.assertNotNull(first);
        Assertions.assertNotNull(first.getGitDiffHeader());
        Assertions.assertEquals("src/com/example/application/data/service/SomeService.java", first.getOldFilename().getFilename());
        Assertions.assertEquals("src/com/example/application/data/service/SomeService.java", first.getNewFilename().getFilename());

        Deque<DiffHunk> hunkList = first.getHunkList();
        Assertions.assertFalse(hunkList.isEmpty());
        Assertions.assertEquals(3, hunkList.size());

        DiffHunk hunk = hunkList.poll();
        Assertions.assertNotNull(hunk);
        Assertions.assertEquals(1, hunk.getFromRange().getLineStart());
        Assertions.assertEquals(6, hunk.getFromRange().getRangeSize());
        Assertions.assertEquals(1, hunk.getToRange().getLineStart());
        Assertions.assertEquals(4, hunk.getToRange().getRangeSize());

        hunk = hunkList.poll();
        Assertions.assertNotNull(hunk);
        Assertions.assertEquals(11, hunk.getFromRange().getLineStart());
        Assertions.assertEquals(6, hunk.getFromRange().getRangeSize());
        Assertions.assertEquals(9, hunk.getToRange().getLineStart());
        Assertions.assertEquals(4, hunk.getToRange().getRangeSize());

        hunk = hunkList.poll();
        Assertions.assertNotNull(hunk);
        Assertions.assertEquals(17, hunk.getFromRange().getLineStart());
        Assertions.assertEquals(1, hunk.getFromRange().getRangeSize());
        Assertions.assertEquals(13, hunk.getToRange().getLineStart());
        Assertions.assertEquals(4, hunk.getToRange().getRangeSize());
    }

}