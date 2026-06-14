package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedContract assigned contract
 */
/**
 * Minimal CONTRACT_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedContract assigned contract
 */
public final class StepContractAssignment implements StepEntity {
    private final int id;
    private final StepContract assignedContract;

    public StepContractAssignment(int id, StepContract assignedContract) {
        this.id = id;
        this.assignedContract = assignedContract;
    }

    public int getId() {
        return id;
    }

    public StepContract getAssignedContract() {
        return assignedContract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepContractAssignment that = (StepContractAssignment) o;
        return id == that.id && Objects.equals(assignedContract, that.assignedContract);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedContract);
    }

    @Override
    public String toString() {
        return "StepContractAssignment{" + "id=" + id + "assignedContract=" + assignedContract + "}";
    }
}
