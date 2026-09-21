package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 * @param items assigned target items
 */
public final class StepAppliedNameAssignment extends AbstractStepEntity {
    private final String assignedName;
    private final List<StepEntity> items;

    public StepAppliedNameAssignment(int id, String assignedName, List<StepEntity> items) {
        super(id, "");
        this.assignedName = assignedName;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getName() {
        return assignedName != null ? assignedName : "";
    }

    public String getAssignedName() {
        return assignedName;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public String namedItem() {
        return assignedName;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedName", assignedName);
        state.put("items", items);
        return state;
    }
}
