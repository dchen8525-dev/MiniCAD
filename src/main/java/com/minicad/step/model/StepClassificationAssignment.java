package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 */
public final class StepClassificationAssignment extends AbstractStepEntity {
    private final StepGroup assignedClass;
    private final StepClassificationRole role;

    public StepClassificationAssignment(int id, StepGroup assignedClass, StepClassificationRole role) {
        super(id, "");
        this.assignedClass = assignedClass;
        this.role = role;
    }

    public StepGroup getAssignedClass() {
        return assignedClass;
    }

    public StepClassificationRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepGroup assignedClass() {
        return assignedClass;
    }

    public StepClassificationRole role() {
        return role;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "assignedClass", assignedClass, "role", role);
    }
}
