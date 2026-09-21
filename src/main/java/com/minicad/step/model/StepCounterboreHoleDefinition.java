package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COUNTERBORE_HOLE_DEFINITION.
 * A counterbore hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param counterboreDiameter diameter of the counterbore
 * @param counterboreDepth depth of the counterbore
 */
public final class StepCounterboreHoleDefinition extends AbstractStepEntity {
    private final StepEntity throughHoleReference;
    private final Double counterboreDiameter;
    private final Double counterboreDepth;

    public StepCounterboreHoleDefinition(int id, String name, StepEntity throughHoleReference, Double counterboreDiameter, Double counterboreDepth) {
        super(id, name);
        this.throughHoleReference = throughHoleReference;
        this.counterboreDiameter = counterboreDiameter;
        this.counterboreDepth = counterboreDepth;
    }

    public StepEntity getThroughHoleReference() {
        return throughHoleReference;
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
        state.put("throughHoleReference", throughHoleReference);
        state.put("counterboreDiameter", counterboreDiameter);
        state.put("counterboreDepth", counterboreDepth);
        return state;
    }
}
