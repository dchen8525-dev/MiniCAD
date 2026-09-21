package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal BOX_DOMAIN.
 *
 * @param id step id
 * @param corner box corner point
 * @param dimensions box dimensions in STEP order
 */
public final class StepBoxDomain extends AbstractStepEntity {
    private final StepCartesianPoint corner;
    private final List<Double> dimensions;

    public StepBoxDomain(int id, String name, StepCartesianPoint corner, List<Double> dimensions) {
        super(id, name != null ? name : "");
        this.corner = corner;
        this.dimensions = dimensions == null ? null : java.util.List.copyOf(dimensions);
    }

    public StepBoxDomain(int id, StepCartesianPoint corner, List<Double> dimensions) {
        this(id, "", corner, dimensions);
    }

    public StepCartesianPoint getCorner() {
        return corner;
    }

    public List<Double> getDimensions() {
        return dimensions;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepCartesianPoint corner() { return getCorner(); }
    public List<Double> dimensions() { return getDimensions(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("corner", corner);
        state.put("dimensions", dimensions);
        return state;
    }
}
