package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved KINEMATIC_JOINT_REFERENCE.
 */
/**
 * Resolved KINEMATIC_JOINT_REFERENCE.
 */
public final class StepKinematicJointReference implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity joint;

    public StepKinematicJointReference(int id, String name, StepEntity joint) {
        this.id = id;
        this.name = name;
        this.joint = joint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getJoint() {
        return joint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKinematicJointReference that = (StepKinematicJointReference) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(joint, that.joint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, joint);
    }

    @Override
    public String toString() {
        return "StepKinematicJointReference{" + "id=" + id + "name=" + name + "joint=" + joint + "}";
    }
}
