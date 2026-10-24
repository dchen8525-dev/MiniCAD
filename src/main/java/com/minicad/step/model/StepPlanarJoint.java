package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved PLANAR_JOINT.
 * A planar joint between two links.
 */
public final class StepPlanarJoint extends AbstractStepEntity {
    private final String description;
    private final StepEntity link1;
    private final StepEntity link2;
    private final StepEntity plane;

    public StepPlanarJoint(int id, String name, String description, StepEntity link1, StepEntity link2, StepEntity plane) {
        super(id, name);
        this.description = description;
        this.link1 = link1;
        this.link2 = link2;
        this.plane = plane;
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

    public StepEntity getPlane() {
        return plane;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "link1", link1, "link2", link2,
                "plane", plane);
    }
}
