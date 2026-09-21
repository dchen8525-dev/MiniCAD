package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepJointConfiguration extends AbstractStepEntity {
    private final StepEntity joint;
    private final String jointType;
    private final double jointPosition;
    private final double jointVelocity;
    private final List<Double> jointLimits;
    private final String jointMotion;

    public StepJointConfiguration(int id, String name, StepEntity joint, String jointType, double jointPosition, double jointVelocity, List<Double> jointLimits, String jointMotion) {
        super(id, name);
        this.joint = joint;
        this.jointType = jointType;
        this.jointPosition = jointPosition;
        this.jointVelocity = jointVelocity;
        this.jointLimits = jointLimits == null ? null : java.util.List.copyOf(jointLimits);
        this.jointMotion = jointMotion;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("joint", joint);
        state.put("jointType", jointType);
        state.put("jointPosition", jointPosition);
        state.put("jointVelocity", jointVelocity);
        state.put("jointLimits", jointLimits);
        state.put("jointMotion", jointMotion);
        return state;
    }
}
