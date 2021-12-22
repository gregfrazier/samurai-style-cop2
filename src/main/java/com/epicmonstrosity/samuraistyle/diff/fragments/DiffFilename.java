package com.epicmonstrosity.samuraistyle.diff.fragments;

import com.epicmonstrosity.samuraistyle.utils.RegExpCaptures;

public class DiffFilename implements Expression {
    private static final String FILENAME_HEADER = "^([\\+\\-]{3})\\s[ab]/([^\\s]+)$";

    private static final String MODIFIED_HEADER = "^[+]{3}\\s(a|b|/dev)/([^\\s]+)$";
    private static final String ORIGINAL_HEADER = "^[\\-]{3}\\s(a|b|/dev)/([^\\s]+)$";

    private String filename;
    private DiffFileType type;

    public static boolean matchOriginal(String line) {
        return line.matches(ORIGINAL_HEADER);
    }

    public static boolean matchModified(String line) {
        return line.matches(MODIFIED_HEADER);
    }

    @Override
    public DiffFilename apply(final String line) {
        final RegExpCaptures groups = new RegExpCaptures(FILENAME_HEADER).input(line);
        groups.getCapture(1).ifPresent(v -> {
            switch (v) {
                case "+++" -> this.setType(DiffFileType.MODIFIED);
                case "---" -> this.setType(DiffFileType.ORIGINAL);
            }
        });
        groups.getCapture(2).ifPresent(this::setFilename);
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public DiffFileType getType() {
        return type;
    }

    private void setFilename(final String filename) {
        this.filename = filename;
    }

    private void setType(final DiffFileType type) {
        this.type = type;
    }
}
