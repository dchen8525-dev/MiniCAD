package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BUCKLING_ANALYSIS.
 * Buckling analysis type for FEA.
 */
public final class StepBucklingAnalysis extends AbstractStepEntity {
    private final int numberOfModes;

    public StepBucklingAnalysis(int id, String name, int numberOfModes) {
        super(id, name);
        this.numberOfModes = numberOfModes;
    }

    public int getNumberOfModes() {
        return numberOfModes;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("numberOfModes", numberOfModes);
        return state;
    }
}
