package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RANGE_DIMENSIONAL_SIZE.
 * Dimensional size with range bounds.
 */
public final class StepRangeDimensionalSize extends AbstractStepEntity {
    private final String description;
    private final double lowerBound;
    private final double upperBound;

    public StepRangeDimensionalSize(int id, String name, String description, double lowerBound, double upperBound) {
        super(id, name);
        this.description = description;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public String getDescription() {
        return description;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("lowerBound", lowerBound);
        state.put("upperBound", upperBound);
        return state;
    }
}
