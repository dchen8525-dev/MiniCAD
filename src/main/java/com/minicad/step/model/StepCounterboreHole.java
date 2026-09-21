package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COUNTERBORE_HOLE.
 * Represents a counterbore hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name counterbore name
 * @param throughHole through hole reference
 * @param counterboreDiameter counterbore diameter
 * @param counterboreDepth counterbore depth
 */
public final class StepCounterboreHole extends AbstractStepEntity {
    private final StepEntity throughHole;
    private final Double counterboreDiameter;
    private final Double counterboreDepth;

    public StepCounterboreHole(int id, String name, StepEntity throughHole, Double counterboreDiameter, Double counterboreDepth) {
        super(id, name);
        this.throughHole = throughHole;
        this.counterboreDiameter = counterboreDiameter;
        this.counterboreDepth = counterboreDepth;
    }

    public StepEntity getThroughHole() {
        return throughHole;
    }

    public Double getCounterboreDiameter() {
        return counterboreDiameter;
    }

    public Double getCounterboreDepth() {
        return counterboreDepth;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("throughHole", throughHole);
        state.put("counterboreDiameter", counterboreDiameter);
        state.put("counterboreDepth", counterboreDepth);
        return state;
    }
}
