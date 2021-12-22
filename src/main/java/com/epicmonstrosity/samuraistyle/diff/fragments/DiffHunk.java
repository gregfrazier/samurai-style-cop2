package com.epicmonstrosity.samuraistyle.diff.fragments;

import com.epicmonstrosity.samuraistyle.utils.RegExpCaptures;

public class DiffHunk implements Expression {
    public static final String HUNK_REGEXP =
            "^@@\\s+-([0-9]+,?[0-9]*)\\s+\\+([0-9]+,?[0-9]*)\\s+@@.*$";
    private DiffRange fromRange;
    private DiffRange toRange;

    public static DiffHunk parseHunk(final String line) {
        final DiffHunk hunk = new DiffHunk();
        return hunk.apply(line);
    }

    private void setFromRange(DiffRange range) {
        this.fromRange = range;
    }

    private void setToRange(DiffRange range) {
        this.toRange = range;
    }

    public DiffRange getFromRange() {
        return fromRange;
    }

    public DiffRange getToRange() {
        return toRange;
    }

    public static boolean match(String line) {
        return line.startsWith("@@");
    }

    @Override
    public DiffHunk apply(String line) {
        final RegExpCaptures input = new RegExpCaptures(HUNK_REGEXP).input(line);
        input.getCapture(1).ifPresent(group -> this.setFromRange(DiffRange.range(group)));
        input.getCapture(2).ifPresent(group -> this.setToRange(DiffRange.range(group)));
        return this;
    }
}

