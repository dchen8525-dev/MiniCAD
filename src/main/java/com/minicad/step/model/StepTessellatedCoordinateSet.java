package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TESSELLATED_COORDINATE_SET.
 * A set of coordinates for tessellated geometry.
 */
public final class StepTessellatedCoordinateSet extends AbstractStepEntity {
    private final List<StepEntity> coordinates;

    public StepTessellatedCoordinateSet(int id, String name, List<StepEntity> coordinates) {
        super(id, name);
        this.coordinates = coordinates == null ? null : java.util.List.copyOf(coordinates);
    }

    public List<StepEntity> getCoordinates() {
        return coordinates;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("coordinates", coordinates);
        return state;
    }
}
