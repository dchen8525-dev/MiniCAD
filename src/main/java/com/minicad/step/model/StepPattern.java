package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PATTERN.
 */
public final class StepPattern extends AbstractStepEntity {
    private final String patternType;
    private final StepEntity seedElement;

    public StepPattern(int id, String name, String patternType, StepEntity seedElement) {
        super(id, name);
        this.patternType = patternType;
        this.seedElement = seedElement;
    }

    public String getPatternType() {
        return patternType;
    }

    public StepEntity getSeedElement() {
        return seedElement;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("patternType", patternType);
        state.put("seedElement", seedElement);
        return state;
    }
}
