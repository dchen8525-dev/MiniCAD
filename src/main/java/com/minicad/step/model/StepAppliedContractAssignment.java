package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedContract assigned contract
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedContract assigned contract
 * @param items assigned target items
 */
public final class StepAppliedContractAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepContract assignedContract;
    private final List<StepEntity> items;

    public StepAppliedContractAssignment(int id, String entityName, StepContract assignedContract, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedContract = assignedContract;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedContractAssignment that = (StepAppliedContractAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedContract, that.assignedContract) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedContract, items);
    }

    @Override
    public String toString() {
        return "StepAppliedContractAssignment{" + "id=" + id + "entityName=" + entityName + "assignedContract=" + assignedContract + "items=" + items + "}";
    }
}
