package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RACK_AND_PINION_PAIR.
 * A rack and pinion kinematic pair coupling rotation and linear translation.
 */
public final class StepRackAndPinionPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity pinion;
    private final StepEntity rack;
    private final Double pitchRadius;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepRackAndPinionPair(int id, String name, String description, StepEntity pinion, StepEntity rack, Double pitchRadius, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.pinion = pinion;
        this.rack = rack;
        this.pitchRadius = pitchRadius;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPinion() {
        return pinion;
    }

    public StepEntity getRack() {
        return rack;
    }

    public Double getPitchRadius() {
        return pitchRadius;
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
        state.put("pinion", pinion);
        state.put("rack", rack);
        state.put("pitchRadius", pitchRadius);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
