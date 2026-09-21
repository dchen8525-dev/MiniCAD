package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GEAR_PAIR_WITH_RANGE.
 * A gear pair with specified range limits.
 */
public final class StepGearPairWithRange extends AbstractStepEntity {
    private final String description;
    private final StepEntity gear1;
    private final StepEntity gear2;
    private final Double ratio;
    private final Double lowerRange;
    private final Double upperRange;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepGearPairWithRange(int id, String name, String description, StepEntity gear1, StepEntity gear2, Double ratio, Double lowerRange, Double upperRange, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.gear1 = gear1;
        this.gear2 = gear2;
        this.ratio = ratio;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getGear1() {
        return gear1;
    }

    public StepEntity getGear2() {
        return gear2;
    }

    public Double getRatio() {
        return ratio;
    }

    public Double getLowerRange() {
        return lowerRange;
    }

    public Double getUpperRange() {
        return upperRange;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("gear1", gear1);
        state.put("gear2", gear2);
        state.put("ratio", ratio);
        state.put("lowerRange", lowerRange);
        state.put("upperRange", upperRange);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
