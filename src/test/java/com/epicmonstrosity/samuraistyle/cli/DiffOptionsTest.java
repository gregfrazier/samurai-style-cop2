package com.epicmonstrosity.samuraistyle.cli;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class DiffOptionsTest {

    @Test
    void testCommandLineStandardInput() {
        String[] args = { "--stdin", "--output", "out.xml" };
        final DiffOptions diffOptions = new DiffOptions();
        new CommandLine(diffOptions).parseArgs(args);
        assertThat(diffOptions.exclusiveInput.stdIn).isTrue();
    }

    @Test
    void testCommandLineExclusiveInput() {
        String[] args = { "--stdin", "--file", "some.diff", "--output", "out.xml" };
        final DiffOptions diffOptions = new DiffOptions();
        assertThrows(CommandLine.MutuallyExclusiveArgsException.class,
                () -> new CommandLine(diffOptions).parseArgs(args));
    }

}