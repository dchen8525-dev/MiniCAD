package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PLANAR_PAIR.
 * A planar kinematic pair allowing translation in a plane and rotation about the plane normal.
 */
public final class StepPlanarPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity planeNormal;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepPlanarPair(int id, String name, String description, StepEntity position, StepEntity planeNormal, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.planeNormal = planeNormal;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getPlaneNormal() {
        return planeNormal;
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
        state.put("planeNormal", planeNormal);
        state.put("link1", link1);
        state.put("link2", link2);
        return state;
    }
}
