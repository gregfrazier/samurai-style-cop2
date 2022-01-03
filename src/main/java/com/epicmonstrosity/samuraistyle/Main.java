package com.epicmonstrosity.samuraistyle;

import com.epicmonstrosity.samuraistyle.cli.DiffOptions;
import com.epicmonstrosity.samuraistyle.diff.UnifiedDiffReader;
import com.epicmonstrosity.samuraistyle.lint.CheckstyleService;
import com.epicmonstrosity.samuraistyle.lint.CheckstyleSuppressor;
import com.epicmonstrosity.samuraistyle.utils.feeds.InputFeed;
import com.epicmonstrosity.samuraistyle.utils.feeds.SimpleFileReader;
import com.epicmonstrosity.samuraistyle.utils.feeds.StandardInputReader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.exit(new Main().execute(args));
    }

    public int execute(String[] args) {
        final DiffOptions diffOptions = new DiffOptions();
        final CommandLine commandLine = new CommandLine(diffOptions);
        try {
            final CommandLine.ParseResult parseResult = commandLine.parseArgs(args);
            final InputFeed diffFeed = getInputFeed(diffOptions);
            final CheckstyleSuppressor suppressor = getSuppressor(diffOptions);
            final UnifiedDiffReader reader = getDiffReader(diffFeed);
            final String xml = suppressor.buildXml(reader.getDocuments());
            writeOutput(diffOptions.getOutputFile(), xml);

            // When using embedded checkstyle
            if (diffOptions.getCheckStyleOptions().useCheckstyle()) {
                final List<File> fileList = suppressor.buildFileList(reader.getDocuments());
                final int errors = new CheckstyleService().scan(fileList, diffOptions.getCheckStyleOptions());
            }

        } catch (CommandLine.MutuallyExclusiveArgsException | CommandLine.MissingParameterException ex) {
            System.out.println(ex.getLocalizedMessage());
            commandLine.usage(System.out);
            return 2;
        } catch (IOException | CheckstyleException e) {
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

    private UnifiedDiffReader getDiffReader(final InputFeed diffFeed) throws IOException {
        return UnifiedDiffReader.parse(diffFeed.readAllLines());
    }

    private CheckstyleSuppressor getSuppressor(DiffOptions diffOptions) {
        return CheckstyleSuppressor.init(diffOptions);
    }

    private InputFeed getInputFeed(DiffOptions diffOptions) {
        InputFeed diffFeed;
        if (Boolean.TRUE.equals(diffOptions.getExclusiveInput().isStdIn()) || diffOptions.getExclusiveInput().getDiff() == null) {
            diffFeed = new StandardInputReader();
        } else {
            diffFeed = new SimpleFileReader(diffOptions.getExclusiveInput().getDiff());
        }
        return diffFeed;
    }
}
