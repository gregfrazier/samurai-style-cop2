package com.epicmonstrosity.samuraistyle.diff.states;

@FunctionalInterface
public interface StateTransition {
    boolean match(final String str);
}
