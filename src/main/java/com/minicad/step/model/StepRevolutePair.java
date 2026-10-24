package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved REVOLUTE_PAIR.
 * A revolute (hinge) kinematic pair allowing rotation about one axis.
 */
public final class StepRevolutePair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity axis;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepRevolutePair(int id, String name, String description, StepEntity position, StepEntity axis, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.axis = axis;
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

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "position", position,
                "axis", axis, "link1", link1, "link2", link2);
    }
}
