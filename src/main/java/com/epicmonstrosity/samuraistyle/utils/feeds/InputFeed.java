package com.epicmonstrosity.samuraistyle.utils.feeds;

import java.io.BufferedReader;
import java.io.IOException;

public interface InputFeed {
    BufferedReader bufferedReader() throws IOException;
    String[] readAllLines() throws IOException;
}
