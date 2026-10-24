package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved TOLERANCE_VALUE.
 * A tolerance value specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param lowerBound lower bound value
 * @param upperBound upper bound value
 */
public final class StepToleranceValue extends AbstractStepEntity {
    private final double lowerBound;
    private final double upperBound;

    public StepToleranceValue(int id, String name, double lowerBound, double upperBound) {
        super(id, name);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "lowerBound", lowerBound, "upperBound", upperBound);
    }
}
