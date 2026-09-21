package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CARTESIAN_POINT.
 *
 * @param id step id
 * @param name step label
 * @param coordinates 2D or 3D coordinates
 */
public final class StepCartesianPoint extends AbstractStepEntity {
    private final List<Double> coordinates;

    public StepCartesianPoint(int id, String name, List<Double> coordinates) {
        super(id, name);
        this.coordinates = coordinates == null ? null : java.util.List.copyOf(coordinates);
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    // Record-style accessor
    public List<Double> coordinates() { return getCoordinates(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("coordinates", coordinates);
        return state;
    }
}
