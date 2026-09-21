package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal ORIENTED_SURFACE parse-only surface wrapper.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceElement referenced surface
 * @param orientation orientation sense
 */
public final class StepOrientedSurface extends AbstractStepEntity {
    private final StepEntity surfaceElement;
    private final boolean orientation;

    public StepOrientedSurface(int id, String name, StepEntity surfaceElement, boolean orientation) {
        super(id, name);
        this.surfaceElement = surfaceElement;
        this.orientation = orientation;
    }

    public StepEntity getSurfaceElement() {
        return surfaceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity surfaceElement() { return getSurfaceElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("surfaceElement", surfaceElement);
        state.put("orientation", orientation);
        return state;
    }
}
