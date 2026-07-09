package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedSecurityClassification assigned security classification
 */
/**
 * Minimal SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedSecurityClassification assigned security classification
 */
public final class StepSecurityClassificationAssignment implements StepEntity {
    private final int id;
    private final StepSecurityClassification assignedSecurityClassification;

    public StepSecurityClassificationAssignment(int id, StepSecurityClassification assignedSecurityClassification) {
        this.id = id;
        this.assignedSecurityClassification = assignedSecurityClassification;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepSecurityClassification getAssignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    // Record-style accessor
    public StepSecurityClassification assignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSecurityClassificationAssignment that = (StepSecurityClassificationAssignment) o;
        return id == that.id && Objects.equals(assignedSecurityClassification, that.assignedSecurityClassification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedSecurityClassification);
    }

    @Override
    public String toString() {
        return "StepSecurityClassificationAssignment{" + "id=" + id + "assignedSecurityClassification=" + assignedSecurityClassification + "}";
    }
}
