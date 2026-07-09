package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved BRANCH_INSTANCE.
 * A branch instance entity.
 *
 * @param id STEP instance id
 * @param name branch instance name
 * @param branchDefinition branch variance definition reference
 * @param branchState branch variance state
 * @param branchResult branch variance result (true/false)
 * @param branchTakenPath branch variance taken path reference
 * @param branchStatus branch variance status
 */
/**
 * Resolved BRANCH_INSTANCE.
 * A branch instance entity.
 *
 * @param id STEP instance id
 * @param name branch instance name
 * @param branchDefinition branch variance definition reference
 * @param branchState branch variance state
 * @param branchResult branch variance result (true/false)
 * @param branchTakenPath branch variance taken path reference
 * @param branchStatus branch variance status
 */
public final class StepBranchInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity branchDefinition;
    private final String branchState;
    private final boolean branchResult;
    private final StepEntity branchTakenPath;
    private final String branchStatus;

    public StepBranchInstance(int id, String name, StepEntity branchDefinition, String branchState, boolean branchResult, StepEntity branchTakenPath, String branchStatus) {
        this.id = id;
        this.name = name;
        this.branchDefinition = branchDefinition;
        this.branchState = branchState;
        this.branchResult = branchResult;
        this.branchTakenPath = branchTakenPath;
        this.branchStatus = branchStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBranchDefinition() {
        return branchDefinition;
    }

    public String getBranchState() {
        return branchState;
    }

    public boolean isBranchResult() {
        return branchResult;
    }

    public StepEntity getBranchTakenPath() {
        return branchTakenPath;
    }

    public String getBranchStatus() {
        return branchStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBranchInstance that = (StepBranchInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(branchDefinition, that.branchDefinition) && Objects.equals(branchState, that.branchState) && branchResult == that.branchResult && Objects.equals(branchTakenPath, that.branchTakenPath) && Objects.equals(branchStatus, that.branchStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, branchDefinition, branchState, branchResult, branchTakenPath, branchStatus);
    }

    @Override
    public String toString() {
        return "StepBranchInstance{" + "id=" + id + "name=" + name + "branchDefinition=" + branchDefinition + "branchState=" + branchState + "branchResult=" + branchResult + "branchTakenPath=" + branchTakenPath + "branchStatus=" + branchStatus + "}";
    }
}