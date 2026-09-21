package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RULE_INSTANCE.
 * A rule instance entity.
 *
 * @param id STEP instance id
 * @param name rule instance name
 * @param ruleDefinition rule variance definition reference
 * @param ruleState rule variance state
 * @param ruleResult rule variance current result
 * @param ruleApplicationCount rule variance application count
 * @param ruleStatus rule variance status
 */
public final class StepRuleInstance extends AbstractStepEntity {
    private final StepEntity ruleDefinition;
    private final String ruleState;
    private final boolean ruleResult;
    private final int ruleApplicationCount;
    private final String ruleStatus;

    public StepRuleInstance(int id, String name, StepEntity ruleDefinition, String ruleState, boolean ruleResult, int ruleApplicationCount, String ruleStatus) {
        super(id, name);
        this.ruleDefinition = ruleDefinition;
        this.ruleState = ruleState;
        this.ruleResult = ruleResult;
        this.ruleApplicationCount = ruleApplicationCount;
        this.ruleStatus = ruleStatus;
    }

    public StepEntity getRuleDefinition() {
        return ruleDefinition;
    }

    public String getRuleState() {
        return ruleState;
    }

    public boolean isRuleResult() {
        return ruleResult;
    }

    public int getRuleApplicationCount() {
        return ruleApplicationCount;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("ruleDefinition", ruleDefinition);
        state.put("ruleState", ruleState);
        state.put("ruleResult", ruleResult);
        state.put("ruleApplicationCount", ruleApplicationCount);
        state.put("ruleStatus", ruleStatus);
        return state;
    }
}
