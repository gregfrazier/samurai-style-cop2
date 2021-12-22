package com.epicmonstrosity.samuraistyle.utils.feeds;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public record SimpleFileReader(File file) implements InputFeed {

    private boolean isFileReady() {
        return file != null && file.exists() && file.isFile() && file.canRead();
    }

    @Override
    public BufferedReader bufferedReader() throws IOException {
        if (isFileReady())
            return Files.newBufferedReader(file.toPath());
        return null;
    }

    @Override
    public String[] readAllLines() throws IOException {
        if (isFileReady())
            return Files.readAllLines(file.toPath()).toArray(String[]::new);
        return new String[0];
    }
}
