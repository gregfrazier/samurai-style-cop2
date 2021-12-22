package com.epicmonstrosity.samuraistyle.diff.states;

import java.util.LinkedHashMap;
import java.util.Map;

public class DiffStateMachine {
    private final Map<DiffState, Map<DiffState, StateTransition>> transitions;
    private DiffState currentState;

    private DiffStateMachine(Map<DiffState, Map<DiffState, StateTransition>> transitions, DiffState startState) {
        this.transitions = transitions;
        this.currentState = startState;
    }

    public DiffState next(final String line) {
        final Map<DiffState, StateTransition> stateTransitionMap = transitions.get(currentState);
        for (Map.Entry<DiffState, StateTransition> transitionEntry : stateTransitionMap.entrySet()) {
            if (transitionEntry.getValue().match(line)) {
                currentState = transitionEntry.getKey();
                return currentState;
            }
        }
        currentState = DiffState.FAILURE;
        return currentState;
    }

    public static class MachineBuilder {
        private final Map<DiffState, Map<DiffState, StateTransition>> transitions = new LinkedHashMap<>();
        private Map<DiffState, StateTransition> selectedState;
        private DiffState inno;

        public MachineBuilder define(DiffState at) {
            if (inno != null && selectedState != null)
                selectedState.putIfAbsent(inno, l -> true);
            selectedState = new LinkedHashMap<>();
            transitions.putIfAbsent(at, selectedState);
            return this;
        }

        public MachineBuilder transition(DiffState to, StateTransition when) {
            selectedState.putIfAbsent(to, when);
            return this;
        }

        public MachineBuilder defaultTransition(DiffState to) {
            inno = to;
            return this;
        }

        public DiffStateMachine startAt(DiffState start) {
            return new DiffStateMachine(transitions, start);
        }
    }
}
