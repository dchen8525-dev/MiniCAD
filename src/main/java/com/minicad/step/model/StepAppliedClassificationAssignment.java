package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedClassificationAssignment extends AbstractStepEntity {
    private final StepGroup assignedClass;
    private final StepClassificationRole role;
    private final List<StepEntity> items;

    public StepAppliedClassificationAssignment(int id, StepGroup assignedClass, StepClassificationRole role, List<StepEntity> items) {
        super(id, "");
        this.assignedClass = assignedClass;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public StepGroup getAssignedClass() {
        return assignedClass;
    }

    public StepClassificationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepGroup assignedClass() {
        return assignedClass;
    }

    public StepClassificationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedClass", assignedClass);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
