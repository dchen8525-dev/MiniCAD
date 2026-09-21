package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CALCULATED_GEOMETRIC_REPRESENTATION_ITEM.
 * A geometric representation item whose values are computed from other geometry.
 */
public final class StepCalculatedGeometricRepresentationItem extends AbstractStepEntity {
    private final StepEntity sourceGeometry;

    public StepCalculatedGeometricRepresentationItem(int id, String name, StepEntity sourceGeometry) {
        super(id, name);
        this.sourceGeometry = sourceGeometry;
    }

    public StepEntity getSourceGeometry() {
        return sourceGeometry;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sourceGeometry", sourceGeometry);
        return state;
    }
}
