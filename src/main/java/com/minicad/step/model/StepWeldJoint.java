package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("jointType", jointType);
        state.put("jointGeometry", jointGeometry);
        state.put("jointParts", jointParts);
        state.put("jointStatus", jointStatus);
        return state;
    }
}
