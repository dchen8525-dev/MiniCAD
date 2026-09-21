package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PLUS_MINUS_TOLERANCE.
 * A plus/minus tolerance specification.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param range tolerance range
 * @param tolerancedMeasure toleranced measure
 */
public final class StepPlusMinusTolerance extends AbstractStepEntity {
    private final StepEntity range;
    private final StepEntity tolerancedMeasure;

    public StepPlusMinusTolerance(int id, String name, StepEntity range, StepEntity tolerancedMeasure) {
        super(id, name);
        this.range = range;
        this.tolerancedMeasure = tolerancedMeasure;
    }

    public StepEntity getRange() {
        return range;
    }

    public StepEntity getTolerancedMeasure() {
        return tolerancedMeasure;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("range", range);
        state.put("tolerancedMeasure", tolerancedMeasure);
        return state;
    }
}
