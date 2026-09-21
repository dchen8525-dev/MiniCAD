package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SURFACE_CURVE with supported 3D curve geometry.
 *
 * @param id step id
 * @param entityName step entity name
 * @param name step label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry associated PCURVE or surface-geometry items
 * @param masterRepresentation preferred representation enum
 */
public final class StepSurfaceCurve extends AbstractStepEntity {
    private final String entityName;
    private final StepEntity curve3d;
    private final List<StepEntity> associatedGeometry;
    private final String masterRepresentation;

    public StepSurfaceCurve(int id, String entityName, String name, StepEntity curve3d, List<StepEntity> associatedGeometry, String masterRepresentation) {
        super(id, name);
        this.entityName = entityName;
        this.curve3d = curve3d;
        this.associatedGeometry = associatedGeometry == null ? null : java.util.List.copyOf(associatedGeometry);
        this.masterRepresentation = masterRepresentation;
    }

    public String getEntityName() {
        return entityName;
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
    public String entityName() { return getEntityName(); }
    public String name() { return getName(); }
    public StepEntity curve3d() { return getCurve3d(); }
    public List<StepEntity> associatedGeometry() { return getAssociatedGeometry(); }
    public String masterRepresentation() { return getMasterRepresentation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("name", getName());
        state.put("curve3d", curve3d);
        state.put("associatedGeometry", associatedGeometry);
        state.put("masterRepresentation", masterRepresentation);
        return state;
    }
}
