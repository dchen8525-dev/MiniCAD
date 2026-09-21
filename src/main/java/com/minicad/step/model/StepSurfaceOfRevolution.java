package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SURFACE_OF_REVOLUTION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve generatrix curve
 * @param axisPosition revolution axis
 */
public final class StepSurfaceOfRevolution extends AbstractStepEntity {
    private final StepEntity sweptCurve;
    private final StepAxis1Placement axisPosition;

    public StepSurfaceOfRevolution(int id, String name, StepEntity sweptCurve, StepAxis1Placement axisPosition) {
        super(id, name);
        this.sweptCurve = sweptCurve;
        this.axisPosition = axisPosition;
    }

    public StepEntity getSweptCurve() {
        return sweptCurve;
    }

    public StepAxis1Placement getAxisPosition() {
        return axisPosition;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity sweptCurve() { return getSweptCurve(); }
    public StepAxis1Placement axisPosition() { return getAxisPosition(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sweptCurve", sweptCurve);
        state.put("axisPosition", axisPosition);
        return state;
    }
}
