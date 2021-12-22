package com.epicmonstrosity.samuraistyle.diff;

import com.epicmonstrosity.samuraistyle.diff.fragments.*;
import com.epicmonstrosity.samuraistyle.diff.states.DiffState;
import com.epicmonstrosity.samuraistyle.diff.states.DiffStateMachine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class UnifiedDiffReader {
    private final DiffStateMachine machine;
    private final Deque<DiffDocument> documents;

    public UnifiedDiffReader() {
        documents = new ArrayDeque<>();

        // Git Specific
        machine = new DiffStateMachine.MachineBuilder()
                .define(DiffState.HEADER)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .defaultTransition(DiffState.HEADER)

                .define(DiffState.GIT_DIFF_HEADER)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.GIT_MODE, GitMode::match)
                .transition(DiffState.GIT_RENAME, GitRename::match)
                .transition(DiffState.GIT_SIMILARITY, GitSimilarity::match)
                .transition(DiffState.GIT_INDEX, GitIndex::match)
                .transition(DiffState.BINARY, DiffBinary::match)
                .transition(DiffState.ORIGINAL_FILENAME, DiffFilename::matchOriginal)

                .define(DiffState.GIT_MODE)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.GIT_MODE, GitMode::match)
                .transition(DiffState.GIT_RENAME, GitRename::match)
                .transition(DiffState.GIT_SIMILARITY, GitSimilarity::match)
                .transition(DiffState.GIT_INDEX, GitIndex::match)
                .transition(DiffState.BINARY, DiffBinary::match)
                .transition(DiffState.ORIGINAL_FILENAME, DiffFilename::matchOriginal)

                .define(DiffState.GIT_RENAME)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.GIT_MODE, GitMode::match)
                .transition(DiffState.GIT_RENAME, GitRename::match)
                .transition(DiffState.GIT_SIMILARITY, GitSimilarity::match)
                .transition(DiffState.GIT_INDEX, GitIndex::match)
                .transition(DiffState.BINARY, DiffBinary::match)
                .transition(DiffState.ORIGINAL_FILENAME, DiffFilename::matchOriginal)

                .define(DiffState.GIT_SIMILARITY)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.GIT_MODE, GitMode::match)
                .transition(DiffState.GIT_RENAME, GitRename::match)
                .transition(DiffState.GIT_SIMILARITY, GitSimilarity::match)
                .transition(DiffState.GIT_INDEX, GitIndex::match)
                .transition(DiffState.BINARY, DiffBinary::match)
                .transition(DiffState.ORIGINAL_FILENAME, DiffFilename::matchOriginal)

                .define(DiffState.GIT_INDEX)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.GIT_MODE, GitMode::match)
                .transition(DiffState.GIT_RENAME, GitRename::match)
                .transition(DiffState.GIT_SIMILARITY, GitSimilarity::match)
                .transition(DiffState.GIT_INDEX, GitIndex::match)
                .transition(DiffState.BINARY, DiffBinary::match)
                .transition(DiffState.ORIGINAL_FILENAME, DiffFilename::matchOriginal)

                .define(DiffState.ORIGINAL_FILENAME)
                .transition(DiffState.MODIFIED_FILENAME, DiffFilename::matchModified)

                .define(DiffState.MODIFIED_FILENAME)
                .transition(DiffState.HUNK, DiffHunk::match)

                .define(DiffState.HUNK)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .defaultTransition(DiffState.CONTENT)

                .define(DiffState.CONTENT)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .transition(DiffState.HUNK, DiffHunk::match)
                .defaultTransition(DiffState.CONTENT)

                .define(DiffState.BINARY)
                .transition(DiffState.GIT_DIFF_HEADER, GitDiffHeader::match)
                .defaultTransition(DiffState.BINARY)

                .define(DiffState.FAILURE)
                .defaultTransition(DiffState.FAILURE)

                .startAt(DiffState.HEADER);
    }

    public void readLines(final String[] lines) {
        for (String line : lines) {
            readLine(line);
        }
    }

    public static UnifiedDiffReader parse(String[] lines) {
        final UnifiedDiffReader diffReader = new UnifiedDiffReader();
        diffReader.readLines(lines);
        return diffReader;
    }

    /**
     * @return Deque of Diff Documents found in diff file
     */
    public Deque<DiffDocument> getDocuments() {
        return documents;
    }

    private void readLine(final String line) {
        // Cheap way to not parse content lines without +/-
        if (line.charAt(0) != 32) {
            final DiffState diffState = machine.next(line);
            // System.out.println(diffState.name() + " >> " + line);
            switch(diffState) {
                case GIT_DIFF_HEADER -> documents.offer(new DiffDocument().setGitDiffHeader(new GitDiffHeader(line)));
                case HUNK -> Optional.ofNullable(documents.peekLast()).map(x -> x.getHunkList().add(DiffHunk.parseHunk(line)));
                case ORIGINAL_FILENAME -> Optional.ofNullable(documents.peekLast()).map(x -> x.setOldFilename(new DiffFilename().apply(line)));
                case MODIFIED_FILENAME -> Optional.ofNullable(documents.peekLast()).map(x -> x.setNewFilename(new DiffFilename().apply(line)));
            }
        }
    }
}
