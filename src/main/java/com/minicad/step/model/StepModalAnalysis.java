package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved MODAL_ANALYSIS.
 * Modal analysis type for FEA.
 */
public final class StepModalAnalysis extends AbstractStepEntity {
    private final int numberOfModes;

    public StepModalAnalysis(int id, String name, int numberOfModes) {
        super(id, name);
        this.numberOfModes = numberOfModes;
    }

    public int getNumberOfModes() {
        return numberOfModes;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "numberOfModes", numberOfModes);
    }
}
