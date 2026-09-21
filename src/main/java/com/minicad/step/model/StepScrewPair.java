package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SCREW_PAIR.
 * A screw kinematic pair coupling rotation and translation along a helical path.
 */
public final class StepScrewPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity axis;
    private final Double pitch;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepScrewPair(int id, String name, String description, StepEntity position, StepEntity axis, Double pitch, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.axis = axis;
        this.pitch = pitch;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getAxis() {
        return axis;
    }

    public Double getPitch() {
        return pitch;
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
        state.put("position", position);
        state.put("axis", axis);
        state.put("pitch", pitch);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
