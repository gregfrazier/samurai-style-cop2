package com.epicmonstrosity.samuraistyle.cli;

import picocli.CommandLine;

public class CheckstyleOptions {
    @CommandLine.Option(names = "--use-checkstyle", description = "Use embedded checkstyle", defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    boolean checkstyle;

    @CommandLine.Option(names = "--checkstyle-config", paramLabel = "CHECKSTYLE_CONFIG_FILE", description = "Checkstyle configuration file", defaultValue = "./sun_checks.xml", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    String config;

    @CommandLine.Option(names = "--checkstyle-report", paramLabel = "REPORT_FILE", description = "Checkstyle report (output) file", defaultValue = "./checkstyle-corrections.xml", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    String outputFilename;

    @CommandLine.Option(names = "--checkstyle-stdout", description = "Print checkstyle output to screen", defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    boolean printToConsole;

    @CommandLine.Option(names = "--checkstyle-format", description = "Checkstyle output format (XML, SARIF, PLAIN)", defaultValue = "XML", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    String outputFormatType;

    public boolean useCheckstyle() {
        return checkstyle;
    }

    public String getConfig() {
        return config;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public boolean isPrintToConsole() {
        return printToConsole;
    }

    public String getOutputFormatType() {
        return outputFormatType;
    }
}
