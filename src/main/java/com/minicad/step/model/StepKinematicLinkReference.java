package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved KINEMATIC_LINK_REFERENCE.
 */
public final class StepKinematicLinkReference extends AbstractStepEntity {
    private final StepEntity link;

    public StepKinematicLinkReference(int id, String name, StepEntity link) {
        super(id, name);
        this.link = link;
    }

    public StepEntity getLink() {
        return link;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("link", link);
        return state;
    }
}
