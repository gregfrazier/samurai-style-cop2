package com.epicmonstrosity.samuraistyle.lint;

import com.epicmonstrosity.samuraistyle.cli.DiffOptions;
import com.epicmonstrosity.samuraistyle.diff.DiffDocument;
import com.epicmonstrosity.samuraistyle.diff.fragments.DiffHunk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckstyleSuppressor {
    private int maxSize = 50000;

    private final static String suppressionHeader =
            "<?xml version=\"1.0\"?><!DOCTYPE suppressions PUBLIC " +
                    "\"-//Puppy Crawl//DTD Suppressions 1.0//EN\" " +
                    "\"http://www.puppycrawl.com/dtds/suppressions_1_0.dtd\">";

    private final static String suppressionContainer = "%s\n" + // Header
            "<suppressions>\n" +
            "%s\n" + // Static Suppressions
            "%s\n" + // Generated Suppressions
            "</suppressions>";

    private final static String suppressLines = "<suppress checks=\".*\" files=\"%s\" lines=\"%s\"/>";
    private final static String excludeCompleteFile = "<suppress checks=\".*\" files=\"%s\" />";

    private final String staticSuppressions;

    public CheckstyleSuppressor(final String staticSuppressions, final Integer maxSize) {
        this.staticSuppressions = staticSuppressions != null && !staticSuppressions.isEmpty()
                ? staticSuppressions : "";
        if (maxSize != null)
            this.maxSize = maxSize;
    }

    public static CheckstyleSuppressor init() {
        return init(null);
    }

    public static CheckstyleSuppressor init(final DiffOptions options) {
        String customSuppressions = "";
        Integer maxSize = null;
        if (options != null) {
            if (options.getStaticSuppressionFile() != null &&
                options.getStaticSuppressionFile().exists()) {
                try {
                    final File suppressionFile = options.getStaticSuppressionFile();
                    customSuppressions = new String(Files.readAllBytes(suppressionFile.toPath()));
                } catch (IOException e) {
                    System.err.println("Unreadable static suppression file, ignoring");
                }
            }
            maxSize = options.getLineLimit();
        }
        return new CheckstyleSuppressor(customSuppressions, maxSize);
    }

    public String buildXml(final Deque<DiffDocument> diffDocuments) {
        return String.format(suppressionContainer, suppressionHeader, staticSuppressions,
                String.join("\n", buildList(diffDocuments)));
    }

    public List<String> buildList(final Deque<DiffDocument> diffDocuments) {
        return diffDocuments.stream()
                .map(this::suppressDiffDocument)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<File> buildFileList(final Deque<DiffDocument> diffDocuments) {
        return diffDocuments.stream()
                .filter(Objects::nonNull)
                .map(x -> x.getNewFilename().getFilename())
                .map(File::new)
                .collect(Collectors.toList());
    }

    private String suppressDiffDocument(DiffDocument diffDocument) {
        if (diffDocument.getNewFilename() != null)
            return suppressDiffHunks(diffDocument.getHunkList(), diffDocument.getNewFilename().getFilename());
        return null;
    }

    private String suppressDiffHunks(final Deque<DiffHunk> hunks, final String filename) {
        final LineNumberRange numberRange = new LineNumberRange(maxSize);
        for (DiffHunk hunk : hunks) {
            if (hunk.getToRange() != null)
                numberRange.exclude(hunk.getToRange().start(), hunk.getToRange().size());
        }

        final String disjointedRange = numberRange.getDisjointedRange();
        if (disjointedRange.isEmpty())
            return String.format(excludeCompleteFile, translateFilename(filename));
        return String.format(suppressLines, translateFilename(filename), disjointedRange);
    }

    private String translateFilename(final String filename) {
        return filename.replaceAll("[\\\\|/]", "[\\\\\\\\/]");
    }
}
