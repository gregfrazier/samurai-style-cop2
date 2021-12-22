package com.epicmonstrosity.samuraistyle.diff;

import com.epicmonstrosity.samuraistyle.diff.fragments.DiffFilename;
import com.epicmonstrosity.samuraistyle.diff.fragments.DiffHunk;
import com.epicmonstrosity.samuraistyle.diff.fragments.GitDiffHeader;
import com.epicmonstrosity.samuraistyle.diff.fragments.GitIndex;

import java.util.ArrayDeque;
import java.util.Deque;

public class DiffDocument {
    private GitDiffHeader gitDiffHeader;
    private GitIndex index;
    private DiffFilename oldFilename;
    private DiffFilename newFilename;
    private Deque<DiffHunk> hunkList = new ArrayDeque<>();

    public GitDiffHeader getGitDiffHeader() {
        return gitDiffHeader;
    }

    public DiffDocument setGitDiffHeader(GitDiffHeader gitDiffHeader) {
        this.gitDiffHeader = gitDiffHeader;
        return this;
    }

    public GitIndex getIndex() {
        return index;
    }

    public DiffDocument setIndex(GitIndex index) {
        this.index = index;
        return this;
    }

    public DiffFilename getOldFilename() {
        return oldFilename;
    }

    public DiffDocument setOldFilename(DiffFilename oldFilename) {
        this.oldFilename = oldFilename;
        return this;
    }

    public DiffFilename getNewFilename() {
        return newFilename;
    }

    public DiffDocument setNewFilename(DiffFilename newFilename) {
        this.newFilename = newFilename;
        return this;
    }

    public Deque<DiffHunk> getHunkList() {
        return hunkList;
    }
}
