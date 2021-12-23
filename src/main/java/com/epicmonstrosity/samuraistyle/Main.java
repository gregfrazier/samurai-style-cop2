package com.epicmonstrosity.samuraistyle;

import com.epicmonstrosity.samuraistyle.cli.DiffOptions;
import com.epicmonstrosity.samuraistyle.diff.UnifiedDiffReader;
import com.epicmonstrosity.samuraistyle.lint.CheckstyleSuppressor;
import com.epicmonstrosity.samuraistyle.utils.feeds.InputFeed;
import com.epicmonstrosity.samuraistyle.utils.feeds.SimpleFileReader;
import com.epicmonstrosity.samuraistyle.utils.feeds.StandardInputReader;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        System.exit(new Main().execute(args));
    }

    public int execute(String[] args) {
        final DiffOptions diffOptions = new DiffOptions();
        final CommandLine commandLine = new CommandLine(diffOptions);
        try {
            final CommandLine.ParseResult parseResult = commandLine.parseArgs(args);
            InputFeed diffFeed = getInputFeed(diffOptions);
            final String xml = generateSuppressionXml(diffOptions, diffFeed);
            writeOutput(diffOptions.getOutputFile(), xml);
        } catch (CommandLine.MutuallyExclusiveArgsException | CommandLine.MissingParameterException ex) {
            System.out.println(ex.getLocalizedMessage());
            commandLine.usage(System.out);
            return 2;
        } catch (IOException e) {
            e.printStackTrace();
            return 3;
        }
        return 0;
    }

    private void writeOutput(File outputFile, String xml) {
        try {
            Files.write(outputFile.toPath(), xml.getBytes());
        } catch (IOException e) {
            System.err.println("Exception occurred when writing output file");
        }
    }

    private String generateSuppressionXml(DiffOptions diffOptions, InputFeed diffFeed) throws IOException {
        final UnifiedDiffReader unifiedDiff = UnifiedDiffReader.parse(diffFeed.readAllLines());
        return CheckstyleSuppressor.init(diffOptions).build(unifiedDiff.getDocuments());
    }

    private InputFeed getInputFeed(DiffOptions diffOptions) {
        InputFeed diffFeed;
        if (diffOptions.getExclusiveInput().isStdIn()) {
            diffFeed = new StandardInputReader();
        } else {
            diffFeed = new SimpleFileReader(diffOptions.getExclusiveInput().getDiff());
        }
        return diffFeed;
    }
}
