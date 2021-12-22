package com.epicmonstrosity.samuraistyle.diff.fragments;

public class DiffBinary {
    public static boolean match(String line) {
        return line.startsWith("Binary ");
    }
}
