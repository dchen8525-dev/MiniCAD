package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DIRECTED_DIMENSIONAL_SIZE.
 * A dimensional size with a direction for tolerance.
 *
 * @param id STEP instance id
 * @param name size name
 * @param magnitude size magnitude
 * @param direction reference direction for the measurement
 */
public final class StepDirectedDimensionalSize extends AbstractStepEntity {
    private final double magnitude;
    private final StepEntity direction;

    public StepDirectedDimensionalSize(int id, String name, double magnitude, StepEntity direction) {
        super(id, name);
        this.magnitude = magnitude;
        this.direction = direction;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "magnitude", magnitude, "direction", direction);
    }
}
