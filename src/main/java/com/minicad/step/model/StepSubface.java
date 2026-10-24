package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved SUBFACE.
 * A sub-face of a connected face set.
 *
 * @param id STEP instance id
 * @param name subface name
 * @param faceElement the underlying face entity
 */
public final class StepSubface extends AbstractStepEntity {
    private final StepEntity faceElement;

    public StepSubface(int id, String name, StepEntity faceElement) {
        super(id, name);
        this.faceElement = faceElement;
    }

    public StepEntity getFaceElement() {
        return faceElement;
    }

    // Record-style accessor
    public StepEntity faceElement() {
        return faceElement;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "faceElement", faceElement);
    }
}
