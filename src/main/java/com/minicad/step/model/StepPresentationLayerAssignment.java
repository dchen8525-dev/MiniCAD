package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal layer assignment.
 *
 * @param id STEP instance id
 * @param name layer name
 * @param description optional layer description
 * @param assignedItems assigned STEP items
 */
public final class StepPresentationLayerAssignment extends AbstractStepEntity {
    private final String description;
    private final List<StepEntity> assignedItems;

    public StepPresentationLayerAssignment(int id, String name, String description, List<StepEntity> assignedItems) {
        super(id, name);
        this.description = description;
        this.assignedItems = assignedItems == null ? null : java.util.List.copyOf(assignedItems);
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getAssignedItems() {
        return assignedItems;
    }

    // Record-style accessor
    public List<StepEntity> assignedItems() {
        return assignedItems;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("assignedItems", assignedItems);
        return state;
    }
}
