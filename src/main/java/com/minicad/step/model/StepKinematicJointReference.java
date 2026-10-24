package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved KINEMATIC_JOINT_REFERENCE.
 */
public final class StepKinematicJointReference extends AbstractStepEntity {
    private final StepEntity joint;

    public StepKinematicJointReference(int id, String name, StepEntity joint) {
        super(id, name);
        this.joint = joint;
    }

    public StepEntity getJoint() {
        return joint;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "joint", joint);
    }
}
