package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 */
public final class StepGroupAssignment extends AbstractStepEntity {
    private final StepGroup assignedGroup;

    public StepGroupAssignment(int id, StepGroup assignedGroup) {
        super(id, "");
        this.assignedGroup = assignedGroup;
    }

    public StepGroup getAssignedGroup() {
        return assignedGroup;
    }

    // Record-style accessor
    public StepGroup assignedGroup() {
        return assignedGroup;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedGroup", assignedGroup);
        return state;
    }
}
