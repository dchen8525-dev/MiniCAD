package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ELLIPSE.
 *
 * @param id step id
 * @param name step label
 * @param position ellipse placement
 * @param semiAxis1 local X semi-axis
 * @param semiAxis2 local Y semi-axis
 */
public final class StepEllipse extends AbstractStepEntity {
    private final StepEntity position;
    private final double semiAxis1;
    private final double semiAxis2;

    public StepEllipse(int id, String name, StepEntity position, double semiAxis1, double semiAxis2) {
        super(id, name);
        this.position = position;
        this.semiAxis1 = semiAxis1;
        this.semiAxis2 = semiAxis2;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getSemiAxis1() {
        return semiAxis1;
    }

    public double getSemiAxis2() {
        return semiAxis2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public double semiAxis1() { return getSemiAxis1(); }
    public double semiAxis2() { return getSemiAxis2(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("semiAxis1", semiAxis1);
        state.put("semiAxis2", semiAxis2);
        return state;
    }
}
