package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved VECTOR.
 *
 * @param id step id
 * @param name step label
 * @param orientation referenced direction
 * @param magnitude vector magnitude
 */
public final class StepVector extends AbstractStepEntity {
    private final StepDirection orientation;
    private final double magnitude;

    public StepVector(int id, String name, StepDirection orientation, double magnitude) {
        super(id, name);
        this.orientation = orientation;
        this.magnitude = magnitude;
    }

    public StepDirection getOrientation() {
        return orientation;
    }

    public StepDirection isOrientation() {
        return orientation;
    }

    public double getMagnitude() {
        return magnitude;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepDirection orientation() { return getOrientation(); }
    public double magnitude() { return getMagnitude(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("orientation", orientation);
        state.put("magnitude", magnitude);
        return state;
    }
}
