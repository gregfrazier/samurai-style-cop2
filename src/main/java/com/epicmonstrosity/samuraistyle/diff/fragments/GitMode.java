package com.epicmonstrosity.samuraistyle.diff.fragments;

public class GitMode {
    public static final String OLD_REGEXP =
            "^old\\smode\\s[0-9A-Fa-f]{6}$";
    public static final String NEW_REGEXP =
            "^new\\smode\\s[0-9A-Fa-f]{6}$";
    public static final String DELETED_FILE_REGEXP =
            "^deleted\\sfile\\smode\\s[0-9A-Fa-f]{6}$";
    public static final String NEW_FILE_REGEXP =
            "^new\\sfile\\smode\\s[0-9A-Fa-f]{6}$";

    public static boolean match(String line) {
        return line.startsWith("old mode ") ||
                line.startsWith("new mode ") ||
                line.startsWith("deleted file mode ") ||
                line.startsWith("new file mode ");
    }
}
