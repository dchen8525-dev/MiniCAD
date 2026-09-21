package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved REPRESENTATION_CONTEXT_3D.
 * A 3D representation context.
 */
public final class StepRepresentationContext3d extends AbstractStepEntity {
    private final String contextType;
    private final List<Double> coordinateSpaceDimensions;

    public StepRepresentationContext3d(int id, String name, String contextType, List<Double> coordinateSpaceDimensions) {
        super(id, name);
        this.contextType = contextType;
        this.coordinateSpaceDimensions = coordinateSpaceDimensions == null ? null : java.util.List.copyOf(coordinateSpaceDimensions);
    }

    public String getContextType() {
        return contextType;
    }

    public List<Double> getCoordinateSpaceDimensions() {
        return coordinateSpaceDimensions;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("contextType", contextType);
        state.put("coordinateSpaceDimensions", coordinateSpaceDimensions);
        return state;
    }
}
