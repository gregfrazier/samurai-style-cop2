package com.epicmonstrosity.samuraistyle.diff.fragments;

public class GitSimilarity {
    public static final String SIMILARITY_REGEXP =
            "^@@\\s+-([0-9]+,?[0-9]*)\\s+\\+([0-9]+,?[0-9]*)\\s+@@.*$";

    public static final String DISSIMILARITY_REGEXP =
            "^@@\\s+-([0-9]+,?[0-9]*)\\s+\\+([0-9]+,?[0-9]*)\\s+@@.*$";

    public static boolean match(String line) {
        return line.startsWith("similarity index ") ||
                line.startsWith("dissimilarity index ");
    }
}
