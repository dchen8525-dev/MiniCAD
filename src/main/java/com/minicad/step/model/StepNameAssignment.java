package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 */
public final class StepNameAssignment extends AbstractStepEntity {
    private final String assignedName;

    public StepNameAssignment(int id, String assignedName) {
        super(id, "");
        this.assignedName = assignedName;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public String getName() {
        return assignedName != null ? assignedName : "";
    }

    // Record-style accessor
    public String namedItem() {
        return assignedName;
    }

    public String assignedName() {
        return assignedName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedName", assignedName);
        return state;
    }
}
