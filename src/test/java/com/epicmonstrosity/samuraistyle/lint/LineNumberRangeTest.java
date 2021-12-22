package com.epicmonstrosity.samuraistyle.lint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineNumberRangeTest {

    @Test
    void check() {
        final LineNumberRange lineNumberRange = new LineNumberRange(50000);
        lineNumberRange.exclude(50, 100);
        lineNumberRange.exclude(234, 1);
        lineNumberRange.exclude(264, 15);
        final String range = lineNumberRange.getDisjointedRange();
        assertEquals("1-49,151-233,236-263,280-50000", range);
    }

    @Test
    void checkStartsExcluded() {
        final LineNumberRange lineNumberRange = new LineNumberRange(50000);
        lineNumberRange.exclude(1, 3);
        lineNumberRange.exclude(45, 5);
        lineNumberRange.exclude(49, 0); // already excluded
        lineNumberRange.exclude(100, 3);
        lineNumberRange.exclude(695, 15);
        final String range = lineNumberRange.getDisjointedRange();
        assertEquals("5-44,51-99,104-694,711-50000", range);
    }

    @Test
    void endsEarly() {
        final LineNumberRange lineNumberRange = new LineNumberRange(300);
        lineNumberRange.exclude(1, 3);
        lineNumberRange.exclude(45, 5);
        lineNumberRange.exclude(49, 0); // already excluded
        lineNumberRange.exclude(100, 3);
        lineNumberRange.exclude(695, 15);
        final String range = lineNumberRange.getDisjointedRange();
        assertEquals("5-44,51-99,104-300", range);
    }

    @Test
    void excludeTheEnd() {
        final LineNumberRange lineNumberRange = new LineNumberRange(500);
        lineNumberRange.exclude(1, 3);
        lineNumberRange.exclude(45, 5);
        lineNumberRange.exclude(49, 0); // already excluded
        lineNumberRange.exclude(100, 3);
        lineNumberRange.exclude(495, 5);
        final String range = lineNumberRange.getDisjointedRange();
        assertEquals("5-44,51-99,104-494", range);
    }

    @Test
    void excludeAll() {
        final LineNumberRange lineNumberRange = new LineNumberRange(500);
        lineNumberRange.exclude(1, 500);
        final String range = lineNumberRange.getDisjointedRange();
        assertEquals("", range);
    }

}