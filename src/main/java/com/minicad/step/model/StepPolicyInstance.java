package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POLICY_INSTANCE.
 * A policy instance entity.
 *
 * @param id STEP instance id
 * @param name policy instance name
 * @param policyDefinition policy variance definition reference
 * @param policyState policy variance state
 * @param policyViolations policy variance violation count
 * @param policyExceptions policy variance exception count
 * @param policyStatus policy variance status
 */
public final class StepPolicyInstance extends AbstractStepEntity {
    private final StepEntity policyDefinition;
    private final String policyState;
    private final int policyViolations;
    private final int policyExceptions;
    private final String policyStatus;

    public StepPolicyInstance(int id, String name, StepEntity policyDefinition, String policyState, int policyViolations, int policyExceptions, String policyStatus) {
        super(id, name);
        this.policyDefinition = policyDefinition;
        this.policyState = policyState;
        this.policyViolations = policyViolations;
        this.policyExceptions = policyExceptions;
        this.policyStatus = policyStatus;
    }

    public StepEntity getPolicyDefinition() {
        return policyDefinition;
    }

    public String getPolicyState() {
        return policyState;
    }

    public int getPolicyViolations() {
        return policyViolations;
    }

    public int getPolicyExceptions() {
        return policyExceptions;
    }

    public String getPolicyStatus() {
        return policyStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("policyDefinition", policyDefinition);
        state.put("policyState", policyState);
        state.put("policyViolations", policyViolations);
        state.put("policyExceptions", policyExceptions);
        state.put("policyStatus", policyStatus);
        return state;
    }
}
