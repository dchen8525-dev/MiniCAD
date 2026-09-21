package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved GENERAL_JOINT.
 * A general joint between two links.
 */
public final class StepGeneralJoint extends AbstractStepEntity {
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepGeneralJoint(int id, String name, String description, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
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

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
