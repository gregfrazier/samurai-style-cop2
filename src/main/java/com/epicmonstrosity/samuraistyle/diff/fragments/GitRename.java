package com.epicmonstrosity.samuraistyle.diff.fragments;

public class GitRename {
    public static final String RENAME_REGEXP =
            "^@@\\s+-([0-9]+,?[0-9]*)\\s+\\+([0-9]+,?[0-9]*)\\s+@@.*$";

    public static boolean match(String line) {
        return line.startsWith("rename from ") ||
                line.startsWith("rename to ");
    }
}
