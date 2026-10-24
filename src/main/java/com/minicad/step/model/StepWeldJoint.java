package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved WELD_JOINT.
 * A weld joint entity.
 *
 * @param id STEP instance id
 * @param name joint name
 * @param jointType joint variance type
 * @param jointGeometry joint variance geometry reference
 * @param jointParts joint variance parts to join
 * @param jointStatus joint variance status
 */
public final class StepWeldJoint extends AbstractStepEntity {
    private final String jointType;
    private final StepEntity jointGeometry;
    private final List<StepEntity> jointParts;
    private final String jointStatus;

    public StepWeldJoint(int id, String name, String jointType, StepEntity jointGeometry, List<StepEntity> jointParts, String jointStatus) {
        super(id, name);
        this.jointType = jointType;
        this.jointGeometry = jointGeometry;
        this.jointParts = jointParts == null ? null : java.util.List.copyOf(jointParts);
        this.jointStatus = jointStatus;
    }

    public String getJointType() {
        return jointType;
    }

    public StepEntity getJointGeometry() {
        return jointGeometry;
    }

    public List<StepEntity> getJointParts() {
        return jointParts;
    }

    public String getJointStatus() {
        return jointStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "jointType", jointType, "jointGeometry", jointGeometry,
                "jointParts", jointParts, "jointStatus", jointStatus);
    }
}
