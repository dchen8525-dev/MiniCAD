package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BOUNDED_CURVE_2D.
 * A 2D curve with bounded extent.
 *
 * @param id step id
 * @param name step label
 * @param curve the underlying 2D curve
 */
public final class StepBoundedCurve2D extends AbstractStepEntity {
    private final StepCurve curve;

    public StepBoundedCurve2D(int id, String name, StepCurve curve) {
        super(id, name);
        this.curve = curve;
    }

    public StepCurve getCurve() {
        return curve;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCurve curve() { return getCurve(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("curve", curve);
        return state;
    }
}
