package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TORUS_VOLUME.
 * A CSG torus primitive volume.
 */
public final class StepTorusVolume extends AbstractStepEntity {
    private final StepEntity position;
    private final Double majorRadius;
    private final Double minorRadius;

    public StepTorusVolume(int id, String name, StepEntity position, Double majorRadius, Double minorRadius) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getMajorRadius() {
        return majorRadius;
    }

    public Double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double majorRadius() { return getMajorRadius(); }
    public Double minorRadius() { return getMinorRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("majorRadius", majorRadius);
        state.put("minorRadius", minorRadius);
        return state;
    }
}
