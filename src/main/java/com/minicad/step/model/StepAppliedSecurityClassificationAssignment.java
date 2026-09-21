package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_SECURITY_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedSecurityClassification assigned security classification
 * @param items assigned target items
 */
public final class StepAppliedSecurityClassificationAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepSecurityClassification assignedSecurityClassification;
    private final List<StepEntity> items;

    public StepAppliedSecurityClassificationAssignment(int id, String entityName, StepSecurityClassification assignedSecurityClassification, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedSecurityClassification = assignedSecurityClassification;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepSecurityClassification getAssignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepSecurityClassification assignedSecurityClassification() {
        return assignedSecurityClassification;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedSecurityClassification", assignedSecurityClassification);
        state.put("items", items);
        return state;
    }
}
