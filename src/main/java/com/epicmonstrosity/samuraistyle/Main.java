package com.epicmonstrosity.samuraistyle;

import com.epicmonstrosity.samuraistyle.cli.DiffOptions;
import com.epicmonstrosity.samuraistyle.diff.UnifiedDiffReader;
import com.epicmonstrosity.samuraistyle.lint.CheckstyleSuppressor;
import com.epicmonstrosity.samuraistyle.utils.feeds.InputFeed;
import com.epicmonstrosity.samuraistyle.utils.feeds.SimpleFileReader;
import com.epicmonstrosity.samuraistyle.utils.feeds.StandardInputReader;
import picocli.CommandLine;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.exit(new Main().execute(args));
    }

    public int execute(String[] args) {
        final DiffOptions diffOptions = new DiffOptions();
        final CommandLine commandLine = new CommandLine(diffOptions);
        try {
            final CommandLine.ParseResult parseResult = commandLine.parseArgs(args);
            InputFeed diffFeed;
            if (diffOptions.getExclusiveInput().isStdIn()) {
                diffFeed = new StandardInputReader();
            } else {
                diffFeed = new SimpleFileReader(diffOptions.getExclusiveInput().getDiff());
            }
            final UnifiedDiffReader unifiedDiff = UnifiedDiffReader.parse(diffFeed.readAllLines());
            final String suppressionFileContents = CheckstyleSuppressor.init(diffOptions).build(unifiedDiff.getDocuments());
            // System.out.println(suppressionFileContents);
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
}
