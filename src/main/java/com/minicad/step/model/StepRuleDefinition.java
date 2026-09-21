package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RULE_DEFINITION.
 * A rule definition entity.
 *
 * @param id STEP instance id
 * @param name rule name
 * @param ruleType rule variance type
 * @param ruleCondition rule variance condition
 * @param ruleActions rule variance actions when true
 * @param rulePriority rule variance priority
 * @param ruleStatus rule variance status
 */
public final class StepRuleDefinition extends AbstractStepEntity {
    private final String ruleType;
    private final String ruleCondition;
    private final List<String> ruleActions;
    private final int rulePriority;
    private final String ruleStatus;

    public StepRuleDefinition(int id, String name, String ruleType, String ruleCondition, List<String> ruleActions, int rulePriority, String ruleStatus) {
        super(id, name);
        this.ruleType = ruleType;
        this.ruleCondition = ruleCondition;
        this.ruleActions = ruleActions == null ? null : java.util.List.copyOf(ruleActions);
        this.rulePriority = rulePriority;
        this.ruleStatus = ruleStatus;
    }

    public String getRuleType() {
        return ruleType;
    }

    public String getRuleCondition() {
        return ruleCondition;
    }

    public List<String> getRuleActions() {
        return ruleActions;
    }

    public int getRulePriority() {
        return rulePriority;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("ruleType", ruleType);
        state.put("ruleCondition", ruleCondition);
        state.put("ruleActions", ruleActions);
        state.put("rulePriority", rulePriority);
        state.put("ruleStatus", ruleStatus);
        return state;
    }
}
