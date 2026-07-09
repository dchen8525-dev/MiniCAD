package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepWeldJoint implements StepEntity {
    private final int id;
    private final String name;
    private final String jointType;
    private final StepEntity jointGeometry;
    private final List<StepEntity> jointParts;
    private final String jointStatus;

    public StepWeldJoint(int id, String name, String jointType, StepEntity jointGeometry, List<StepEntity> jointParts, String jointStatus) {
        this.id = id;
        this.name = name;
        this.jointType = jointType;
        this.jointGeometry = jointGeometry;
        this.jointParts = jointParts == null ? null : java.util.List.copyOf(jointParts);
        this.jointStatus = jointStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWeldJoint that = (StepWeldJoint) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(jointType, that.jointType) && Objects.equals(jointGeometry, that.jointGeometry) && Objects.equals(jointParts, that.jointParts) && Objects.equals(jointStatus, that.jointStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, jointType, jointGeometry, jointParts, jointStatus);
    }

    @Override
    public String toString() {
        return "StepWeldJoint{" + "id=" + id + "name=" + name + "jointType=" + jointType + "jointGeometry=" + jointGeometry + "jointParts=" + jointParts + "jointStatus=" + jointStatus + "}";
    }
}