package com.epicmonstrosity.samuraistyle.cli;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "samuraistyle", showDefaultValues = true)
public class DiffOptions {

    @CommandLine.ArgGroup(heading = "Diff Input", multiplicity = "1")
    ExclusiveInput exclusiveInput;

    @CommandLine.Mixin
    CheckstyleOptions checkStyleOptions;

    @CommandLine.Option(names = { "-o", "--output" }, paramLabel = "FILE",  description = "Suppression file output", defaultValue = "./suppression.xml")
    File outputFile;

    @CommandLine.Option(names = { "--static" }, paramLabel = "FILE", description = "Static suppression file")
    File staticSuppressionFile;

    @CommandLine.Option(names = { "--limit" }, description = "Largest line number", defaultValue = "50000")
    Integer lineLimit;

    public Integer getLineLimit() {
        return lineLimit;
    }

    public File getStaticSuppressionFile() {
        return staticSuppressionFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public ExclusiveInput getExclusiveInput() {
        return exclusiveInput;
    }

    public CheckstyleOptions getCheckStyleOptions() {
        return checkStyleOptions;
    }

    public static class ExclusiveInput {
        @CommandLine.Option(names = "--stdin", description = "Use Standard In for diff input", required = true)
        Boolean stdIn;

        @CommandLine.Option(names = { "-f", "--file" }, paramLabel = "DIFF_FILE", description = "Use diff file", required = true)
        File diff;

        public Boolean isStdIn() {
            return stdIn;
        }

        public File getDiff() {
            return diff;
        }
    }
}
