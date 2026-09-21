package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ORIENTED_CURVE parse-only curve wrapper.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param curveElement referenced curve
 * @param orientation orientation sense
 */
public final class StepOrientedCurve extends AbstractStepEntity {
    private final StepEntity curveElement;
    private final boolean orientation;

    public StepOrientedCurve(int id, String name, StepEntity curveElement, boolean orientation) {
        super(id, name);
        this.curveElement = curveElement;
        this.orientation = orientation;
    }

    public StepEntity getCurveElement() {
        return curveElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity curveElement() { return getCurveElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("curveElement", curveElement);
        state.put("orientation", orientation);
        return state;
    }
}
