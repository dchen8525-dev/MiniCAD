package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_CERTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedCertification assigned certification
 * @param items assigned target items
 */
public final class StepAppliedCertificationAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepCertification assignedCertification;
    private final List<StepEntity> items;

    public StepAppliedCertificationAssignment(int id, String entityName, StepCertification assignedCertification, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedCertification = assignedCertification;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepCertification getAssignedCertification() {
        return assignedCertification;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepCertification assignedCertification() {
        return assignedCertification;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedCertification", assignedCertification);
        state.put("items", items);
        return state;
    }
}
