package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STUD.
 * Represents a stud/protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name stud name
 * @param profile profile definition
 * @param height stud height
 * @param direction stud direction
 */
public final class StepStud extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;

    public StepStud(int id, String name, StepEntity profile, Double height, StepEntity direction) {
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
