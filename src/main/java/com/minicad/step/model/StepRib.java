package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RIB.
 * Represents a rib feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 */
public final class StepRib extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;

    public StepRib(int id, String name, StepEntity profile, Double height, StepEntity direction) {
        super(id, name);
        this.profile = profile;
        this.height = height;
        this.direction = direction;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getHeight() {
        return height;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("height", height);
        state.put("direction", direction);
        return state;
    }
}
