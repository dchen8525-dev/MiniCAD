package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COUNTERSINK_HOLE.
 * Represents a countersink hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name countersink name
 * @param throughHole through hole reference
 * @param countersinkDiameter countersink diameter
 * @param countersinkAngle countersink angle
 */
public final class StepCountersinkHole extends AbstractStepEntity {
    private final StepEntity throughHole;
    private final Double countersinkDiameter;
    private final Double countersinkAngle;

    public StepCountersinkHole(int id, String name, StepEntity throughHole, Double countersinkDiameter, Double countersinkAngle) {
        super(id, name);
        this.throughHole = throughHole;
        this.countersinkDiameter = countersinkDiameter;
        this.countersinkAngle = countersinkAngle;
    }

    public StepEntity getThroughHole() {
        return throughHole;
    }

    public Double getCountersinkDiameter() {
        return countersinkDiameter;
    }

    public Double getCountersinkAngle() {
        return countersinkAngle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("throughHole", throughHole);
        state.put("countersinkDiameter", countersinkDiameter);
        state.put("countersinkAngle", countersinkAngle);
        return state;
    }
}
