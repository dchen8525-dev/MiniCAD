package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedContract assigned contract
 * @param items assigned target items
 */
public final class StepAppliedContractAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepContract assignedContract;
    private final List<StepEntity> items;

    public StepAppliedContractAssignment(int id, String entityName, StepContract assignedContract, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedContract = assignedContract;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepContract getAssignedContract() {
        return assignedContract;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepContract assignedContract() {
        return assignedContract;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedContract", assignedContract);
        state.put("items", items);
        return state;
    }
}
