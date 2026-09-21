package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SCREW_JOINT.
 * A screw joint between two links.
 */
public final class StepScrewJoint extends AbstractStepEntity {
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;
    private final double pitch;

    public StepScrewJoint(int id, String name, String description, StepEntity link1, StepEntity link2, double pitch) {
        super(id, name);
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.pitch = pitch;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    public double getPitch() {
        return pitch;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("link1", link1);
        state.put("link2", link2);
        state.put("pitch", pitch);
        return state;
    }
}
