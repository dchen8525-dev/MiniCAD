package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POLY_LOOP.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param polygon polygon points
 */
public final class StepPolyLoop extends AbstractStepEntity implements StepLoop {
    private final List<StepCartesianPoint> polygon;

    public StepPolyLoop(int id, String name, List<StepCartesianPoint> polygon) {
        super(id, name);
        this.polygon = polygon == null ? null : java.util.List.copyOf(polygon);
    }

    public List<StepCartesianPoint> getPolygon() {
        return polygon;
    }

    // Record-style accessor
    public List<StepCartesianPoint> polygon() {
        return polygon;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("polygon", polygon);
        return state;
    }
}
