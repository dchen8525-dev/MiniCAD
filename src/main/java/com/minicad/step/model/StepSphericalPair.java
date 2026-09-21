package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SPHERICAL_PAIR.
 * A spherical (ball-and-socket) kinematic pair allowing rotation about three axes.
 */
public final class StepSphericalPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepSphericalPair(int id, String name, String description, StepEntity position, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
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
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
