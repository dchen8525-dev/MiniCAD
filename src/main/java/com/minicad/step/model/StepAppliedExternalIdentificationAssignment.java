package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 * @param items assigned target items
 */
public final class StepAppliedExternalIdentificationAssignment extends AbstractStepEntity {
    private final String assignedId;
    private final StepIdentificationRole role;
    private final StepExternalSource source;
    private final List<StepEntity> items;

    public StepAppliedExternalIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, StepExternalSource source, List<StepEntity> items) {
        super(id, "");
        this.assignedId = assignedId;
        this.role = role;
        this.source = source;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getName() {
        return assignedId != null ? assignedId : "";
    }

    public String getAssignedId() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    public StepExternalSource getSource() {
        return source;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepIdentificationRole role() {
        return role;
    }

    public StepExternalSource source() {
        return source;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedId", assignedId);
        state.put("role", role);
        state.put("source", source);
        state.put("items", items);
        return state;
    }
}
