package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved UNIVERSAL_PAIR.
 * A universal (Hooke's joint) kinematic pair allowing rotation about two intersecting axes.
 */
public final class StepUniversalPair extends AbstractStepEntity {
    private final String description;
    private final StepEntity position;
    private final StepEntity axis1;
    private final StepEntity axis2;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepUniversalPair(int id, String name, String description, StepEntity position, StepEntity axis1, StepEntity axis2, StepEntity link1, StepEntity link2) {
        super(id, name);
        this.description = description;
        this.position = position;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.link1 = link1;
        this.link2 = link2;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getAxis1() {
        return axis1;
    }

    public StepEntity getAxis2() {
        return axis2;
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
                "axis1", axis1, "axis2", axis2, "link1", link1, "link2", link2);
    }
}
