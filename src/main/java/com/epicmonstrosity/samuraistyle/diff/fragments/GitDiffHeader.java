package com.epicmonstrosity.samuraistyle.diff.fragments;

public class GitDiffHeader {
    private final String headerLine;

    public GitDiffHeader(final String headerLine) {
        this.headerLine = headerLine;
    }

    public static boolean match(String line) {
        return line.startsWith("diff --git ");
    }
}
