package com.epicmonstrosity.samuraistyle.utils.feeds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StandardInputReader implements InputFeed {
    @Override
    public BufferedReader bufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String[] readAllLines() throws IOException {
        final BufferedReader bufferedReader = bufferedReader();
        return bufferedReader.lines().toArray(String[]::new);
    }
}
