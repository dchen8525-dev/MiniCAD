package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BRANCH_DEFINITION.
 * A branch definition entity.
 *
 * @param id STEP instance id
 * @param name branch name
 * @param branchType branch variance type
 * @param branchCondition branch variance condition
 * @param branchTrue branch variance true path reference
 * @param branchFalse branch variance false path reference
 * @param branchStatus branch variance status
 */
/**
 * Resolved BRANCH_DEFINITION.
 * A branch definition entity.
 *
 * @param id STEP instance id
 * @param name branch name
 * @param branchType branch variance type
 * @param branchCondition branch variance condition
 * @param branchTrue branch variance true path reference
 * @param branchFalse branch variance false path reference
 * @param branchStatus branch variance status
 */
public final class StepBranchDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String branchType;
    private final String branchCondition;
    private final StepEntity branchTrue;
    private final StepEntity branchFalse;
    private final String branchStatus;

    public StepBranchDefinition(int id, String name, String branchType, String branchCondition, StepEntity branchTrue, StepEntity branchFalse, String branchStatus) {
        this.id = id;
        this.name = name;
        this.branchType = branchType;
        this.branchCondition = branchCondition;
        this.branchTrue = branchTrue;
        this.branchFalse = branchFalse;
        this.branchStatus = branchStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBranchType() {
        return branchType;
    }

    public String getBranchCondition() {
        return branchCondition;
    }

    public StepEntity getBranchTrue() {
        return branchTrue;
    }

    public StepEntity getBranchFalse() {
        return branchFalse;
    }

    public String getBranchStatus() {
        return branchStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBranchDefinition that = (StepBranchDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(branchType, that.branchType) && Objects.equals(branchCondition, that.branchCondition) && Objects.equals(branchTrue, that.branchTrue) && Objects.equals(branchFalse, that.branchFalse) && Objects.equals(branchStatus, that.branchStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, branchType, branchCondition, branchTrue, branchFalse, branchStatus);
    }

    @Override
    public String toString() {
        return "StepBranchDefinition{" + "id=" + id + "name=" + name + "branchType=" + branchType + "branchCondition=" + branchCondition + "branchTrue=" + branchTrue + "branchFalse=" + branchFalse + "branchStatus=" + branchStatus + "}";
    }
}