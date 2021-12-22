package com.epicmonstrosity.samuraistyle.utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpCaptures {
    private final Pattern pattern;
    private Matcher match;

    public RegExpCaptures(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public RegExpCaptures input(final String input) {
        match = pattern.matcher(input);
        return this;
    }

    public Optional<String> getCapture(final int groupNum) {
        if(match.matches())
            return Optional.ofNullable(match.replaceFirst(String.format("$%d", groupNum)));
        return Optional.empty();
    }

    public Optional<String> getCapture(final int groupNum, final String def) {
        if(match.matches())
            return Optional.ofNullable(match.replaceFirst(String.format("$%d", groupNum)));
        return Optional.ofNullable(def);
    }
}
