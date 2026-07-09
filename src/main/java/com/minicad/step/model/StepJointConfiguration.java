package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved JOINT_CONFIGURATION.
 * A joint configuration entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param joint reference kinematic joint
 * @param jointType joint type (revolute, prismatic, spherical)
 * @param jointPosition joint position/angle value
 * @param jointVelocity joint velocity
 * @param jointLimits joint limit values
 * @param jointMotion joint motion direction
 */
/**
 * Resolved JOINT_CONFIGURATION.
 * A joint configuration entity.
 *
 * @param id STEP instance id
 * @param name configuration name
 * @param joint reference kinematic joint
 * @param jointType joint type (revolute, prismatic, spherical)
 * @param jointPosition joint position/angle value
 * @param jointVelocity joint velocity
 * @param jointLimits joint limit values
 * @param jointMotion joint motion direction
 */
public final class StepJointConfiguration implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity joint;
    private final String jointType;
    private final double jointPosition;
    private final double jointVelocity;
    private final List<Double> jointLimits;
    private final String jointMotion;

    public StepJointConfiguration(int id, String name, StepEntity joint, String jointType, double jointPosition, double jointVelocity, List<Double> jointLimits, String jointMotion) {
        this.id = id;
        this.name = name;
        this.joint = joint;
        this.jointType = jointType;
        this.jointPosition = jointPosition;
        this.jointVelocity = jointVelocity;
        this.jointLimits = jointLimits == null ? null : java.util.List.copyOf(jointLimits);
        this.jointMotion = jointMotion;
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

    public String getJointType() {
        return jointType;
    }

    public double getJointPosition() {
        return jointPosition;
    }

    public double getJointVelocity() {
        return jointVelocity;
    }

    public List<Double> getJointLimits() {
        return jointLimits;
    }

    public String getJointMotion() {
        return jointMotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepJointConfiguration that = (StepJointConfiguration) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(joint, that.joint) && Objects.equals(jointType, that.jointType) && jointPosition == that.jointPosition && jointVelocity == that.jointVelocity && Objects.equals(jointLimits, that.jointLimits) && Objects.equals(jointMotion, that.jointMotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, joint, jointType, jointPosition, jointVelocity, jointLimits, jointMotion);
    }

    @Override
    public String toString() {
        return "StepJointConfiguration{" + "id=" + id + "name=" + name + "joint=" + joint + "jointType=" + jointType + "jointPosition=" + jointPosition + "jointVelocity=" + jointVelocity + "jointLimits=" + jointLimits + "jointMotion=" + jointMotion + "}";
    }
}