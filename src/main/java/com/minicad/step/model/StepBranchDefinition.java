package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepBranchDefinition extends AbstractStepEntity {
    private final String branchType;
    private final String branchCondition;
    private final StepEntity branchTrue;
    private final StepEntity branchFalse;
    private final String branchStatus;

    public StepBranchDefinition(int id, String name, String branchType, String branchCondition, StepEntity branchTrue, StepEntity branchFalse, String branchStatus) {
        super(id, name);
        this.branchType = branchType;
        this.branchCondition = branchCondition;
        this.branchTrue = branchTrue;
        this.branchFalse = branchFalse;
        this.branchStatus = branchStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("branchType", branchType);
        state.put("branchCondition", branchCondition);
        state.put("branchTrue", branchTrue);
        state.put("branchFalse", branchFalse);
        state.put("branchStatus", branchStatus);
        return state;
    }
}
