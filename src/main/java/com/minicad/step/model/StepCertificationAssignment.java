package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedCertification assigned certification
 */
/**
 * Minimal CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedCertification assigned certification
 */
public final class StepCertificationAssignment implements StepEntity {
    private final int id;
    private final StepCertification assignedCertification;

    public StepCertificationAssignment(int id, StepCertification assignedCertification) {
        this.id = id;
        this.assignedCertification = assignedCertification;
    }

    public int getId() {
        return id;
    }

    public StepCertification getAssignedCertification() {
        return assignedCertification;
    }

    public String getName() {
        return "";
    }

    // Record-style accessor - no name field, return empty string
    public String name() {
        return "";
    }

    public StepCertification assignedCertification() {
        return assignedCertification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCertificationAssignment that = (StepCertificationAssignment) o;
        return id == that.id && Objects.equals(assignedCertification, that.assignedCertification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedCertification);
    }

    @Override
    public String toString() {
        return "StepCertificationAssignment{" + "id=" + id + "assignedCertification=" + assignedCertification + "}";
    }
}
