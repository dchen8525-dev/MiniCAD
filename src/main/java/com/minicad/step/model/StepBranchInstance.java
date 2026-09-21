package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepBranchInstance extends AbstractStepEntity {
    private final StepEntity branchDefinition;
    private final String branchState;
    private final boolean branchResult;
    private final StepEntity branchTakenPath;
    private final String branchStatus;

    public StepBranchInstance(int id, String name, StepEntity branchDefinition, String branchState, boolean branchResult, StepEntity branchTakenPath, String branchStatus) {
        super(id, name);
        this.branchDefinition = branchDefinition;
        this.branchState = branchState;
        this.branchResult = branchResult;
        this.branchTakenPath = branchTakenPath;
        this.branchStatus = branchStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("branchDefinition", branchDefinition);
        state.put("branchState", branchState);
        state.put("branchResult", branchResult);
        state.put("branchTakenPath", branchTakenPath);
        state.put("branchStatus", branchStatus);
        return state;
    }
}
