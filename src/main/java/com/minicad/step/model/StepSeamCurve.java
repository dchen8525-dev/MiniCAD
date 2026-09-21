package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal resolved SEAM_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry seam-associated PCURVE items
 * @param masterRepresentation preferred representation enum
 */
public final class StepSeamCurve extends AbstractStepEntity {
    private final StepEntity curve3d;
    private final List<StepEntity> associatedGeometry;
    private final String masterRepresentation;

    public StepSeamCurve(int id, String name, StepEntity curve3d, List<StepEntity> associatedGeometry, String masterRepresentation) {
        super(id, name);
        this.curve3d = curve3d;
        this.associatedGeometry = associatedGeometry == null ? null : java.util.List.copyOf(associatedGeometry);
        this.masterRepresentation = masterRepresentation;
    }

    public StepEntity getCurve3d() {
        return curve3d;
    }

    public List<StepEntity> getAssociatedGeometry() {
        return associatedGeometry;
    }

    public String getMasterRepresentation() {
        return masterRepresentation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity curve3d() { return getCurve3d(); }
    public List<StepEntity> associatedGeometry() { return getAssociatedGeometry(); }
    public String masterRepresentation() { return getMasterRepresentation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("curve3d", curve3d);
        state.put("associatedGeometry", associatedGeometry);
        state.put("masterRepresentation", masterRepresentation);
        return state;
    }
}
