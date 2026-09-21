package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LINEAR_PATTERN.
 * Represents a linear pattern feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pattern name
 * @param baseFeature base feature being patterned
 * @param direction pattern direction
 * @param spacing spacing between features
 * @param count number of features
 */
public final class StepLinearPattern extends AbstractStepEntity {
    private final StepEntity baseFeature;
    private final StepEntity direction;
    private final Double spacing;
    private final Integer count;

    public StepLinearPattern(int id, String name, StepEntity baseFeature, StepEntity direction, Double spacing, Integer count) {
        super(id, name);
        this.baseFeature = baseFeature;
        this.direction = direction;
        this.spacing = spacing;
        this.count = count;
    }

    public StepEntity getBaseFeature() {
        return baseFeature;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getSpacing() {
        return spacing;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("baseFeature", baseFeature);
        state.put("direction", direction);
        state.put("spacing", spacing);
        state.put("count", count);
        return state;
    }
}
