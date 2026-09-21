package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal point set.
 *
 * @param id STEP instance id
 * @param name set name
 * @param points point elements
 */
public final class StepPointSet extends AbstractStepEntity {
    private final List<StepEntity> points;

    public StepPointSet(int id, String name, List<StepEntity> points) {
        super(id, name);
        this.points = points == null ? null : java.util.List.copyOf(points);
    }

    public List<StepEntity> getPoints() {
        return points;
    }

    // Record-style accessor
    public List<StepEntity> points() {
        return points;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("points", points);
        return state;
    }
}
