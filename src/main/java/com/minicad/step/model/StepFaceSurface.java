package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FACE_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param bounds face bounds
 * @param faceGeometry supporting surface
 * @param sameSense orientation flag
 */
public final class StepFaceSurface extends AbstractStepEntity implements StepFaceEntity {
    private final List<StepFaceBound> bounds;
    private final StepEntity faceGeometry;
    private final boolean sameSense;

    public StepFaceSurface(int id, String name, List<StepFaceBound> bounds, StepEntity faceGeometry, boolean sameSense) {
        super(id, name);
        this.bounds = bounds == null ? null : java.util.List.copyOf(bounds);
        this.faceGeometry = faceGeometry;
        this.sameSense = sameSense;
    }

    public List<StepFaceBound> getBounds() {
        return bounds;
    }

    public StepEntity getFaceGeometry() {
        return faceGeometry;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepFaceBound> bounds() { return getBounds(); }
    public StepEntity faceGeometry() { return getFaceGeometry(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("bounds", bounds);
        state.put("faceGeometry", faceGeometry);
        state.put("sameSense", sameSense);
        return state;
    }
}
