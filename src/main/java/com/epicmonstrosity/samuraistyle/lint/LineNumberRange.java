package com.epicmonstrosity.samuraistyle.lint;

import com.epicmonstrosity.samuraistyle.utils.common.IntRange;

import java.util.*;
import java.util.stream.Collectors;

public class LineNumberRange {
    private final int end;
    private final Map<Integer, Integer> exclusions = new TreeMap<>();

    public LineNumberRange(final int maxLength) {
        this.end = maxLength;
    }

    public void exclude(final int lineNumber, final int length) {
        exclusions.putIfAbsent(lineNumber, length);
    }

    public String getDisjointedRange() {
        List<IntRange> suppressionRanges = new ArrayList<>();
        int trackingLine = 1;
        for (Map.Entry<Integer, Integer> excluded : exclusions.entrySet()) {
            final int excludedLineNumber = excluded.getKey(); // inclusive
            final int excludedLineNumberEnd = excludedLineNumber + excluded.getValue(); // exclusive
            if (excludedLineNumber > end)
                continue;
            if (trackingLine < excludedLineNumber) {
                suppressionRanges.add(new IntRange(trackingLine, excludedLineNumber - 1));
                trackingLine = excludedLineNumberEnd + 1;
            } else if (trackingLine == excludedLineNumber) {
                trackingLine = excludedLineNumberEnd + 1;
            }
        }
        if (trackingLine < end)
            suppressionRanges.add(new IntRange(trackingLine, end));
        return suppressionRanges.stream()
                .map(x -> "%d-%d".formatted(x.start(), x.end()))
                .collect(Collectors.joining(","));
    }
}
