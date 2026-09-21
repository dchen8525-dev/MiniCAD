package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 * @param items assigned target items
 */
public final class StepAppliedGroupAssignment extends AbstractStepEntity {
    private final StepGroup assignedGroup;
    private final List<StepEntity> items;

    public StepAppliedGroupAssignment(int id, StepGroup assignedGroup, List<StepEntity> items) {
        super(id, "");
        this.assignedGroup = assignedGroup;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public StepGroup getAssignedGroup() {
        return assignedGroup;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getName() {
        return assignedGroup != null ? assignedGroup.getName() : "";
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public StepGroup assignedGroup() {
        return assignedGroup;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedGroup", assignedGroup);
        state.put("items", items);
        return state;
    }
}
