package com.epicmonstrosity.samuraistyle.diff.states;

public enum DiffState {
    INITIALIZE,
    HEADER,
    ORIGINAL_FILENAME,
    MODIFIED_FILENAME,
    HUNK,
    CONTENT,
    FAILURE,
    BINARY,
    INNOCUOUS,
    // Git Extended
    GIT_DIFF_HEADER,
    GIT_MODE,
    GIT_RENAME,
    GIT_SIMILARITY,
    GIT_INDEX
}
