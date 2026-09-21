package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedClass", assignedClass);
        state.put("role", role);
        return state;
    }
}
