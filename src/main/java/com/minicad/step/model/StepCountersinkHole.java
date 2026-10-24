package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "throughHole", throughHole,
                "countersinkDiameter", countersinkDiameter, "countersinkAngle", countersinkAngle);
    }
}
