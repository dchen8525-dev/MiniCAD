package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GEAR_PAIR.
 * A gear kinematic pair linking two revolute pairs with a gear ratio.
 */
public final class StepGearPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity gear1;
    private final StepEntity gear2;
    private final Double ratio;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepGearPair(int id, String name, String description, StepEntity gear1, StepEntity gear2, Double ratio, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.gear1 = gear1;
        this.gear2 = gear2;
        this.ratio = ratio;
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
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
