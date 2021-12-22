package com.epicmonstrosity.samuraistyle.diff.fragments;

public class GitIndex {
    public static boolean match(String line) {
        return line.startsWith("index ");
    }
}
