package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved ORIENTED_FACE.
 *
 * @param id step id
 * @param name step label
 * @param faceElement referenced base face
 * @param orientation orientation flag
 */
public final class StepOrientedFace extends AbstractStepEntity implements StepFaceEntity {
    private final StepFaceEntity faceElement;
    private final boolean orientation;

    public StepOrientedFace(int id, String name, StepFaceEntity faceElement, boolean orientation) {
        super(id, name);
        this.faceElement = faceElement;
        this.orientation = orientation;
    }

    public StepFaceEntity getFaceElement() {
        return faceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // StepFaceEntity interface implementation
    @Override
    public List<StepFaceBound> bounds() {
        return faceElement != null ? faceElement.getBounds() : List.of();
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepFaceEntity faceElement() { return getFaceElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "faceElement", faceElement, "orientation", orientation);
    }
}
