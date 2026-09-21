package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ORIENTED_SUBFACE.
 * An oriented reference to a sub-face.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying subface entity
 * @param orientation orientation flag
 */
public final class StepOrientedSubface extends AbstractStepEntity {
    private final StepEntity faceElement;
    private final boolean orientation;

    public StepOrientedSubface(int id, String name, StepEntity faceElement, boolean orientation) {
        super(id, name);
        this.faceElement = faceElement;
        this.orientation = orientation;
    }

    public StepEntity getFaceElement() {
        return faceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public StepEntity faceElement() {
        return faceElement;
    }

    public boolean orientation() {
        return orientation;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("faceElement", faceElement);
        state.put("orientation", orientation);
        return state;
    }
}
