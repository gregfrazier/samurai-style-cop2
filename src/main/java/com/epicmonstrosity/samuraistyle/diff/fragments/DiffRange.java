package com.epicmonstrosity.samuraistyle.diff.fragments;

public record DiffRange(int start, int size) {
    public static DiffRange range(final String rangeStr) {
        final String[] split = rangeStr.split(",", 2);
        final int start = Integer.parseInt(split[0]);
        int size = 1;
        if (split.length > 1)
            size = Integer.parseInt(split[1]);
        return new DiffRange(start, size);
    }

    public int getLineStart() {
        return start;
    }

    public int getRangeSize() {
        return size;
    }
}
