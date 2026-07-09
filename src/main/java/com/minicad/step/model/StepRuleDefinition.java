package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRuleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String ruleType;
    private final String ruleCondition;
    private final List<String> ruleActions;
    private final int rulePriority;
    private final String ruleStatus;

    public StepRuleDefinition(int id, String name, String ruleType, String ruleCondition, List<String> ruleActions, int rulePriority, String ruleStatus) {
        this.id = id;
        this.name = name;
        this.ruleType = ruleType;
        this.ruleCondition = ruleCondition;
        this.ruleActions = ruleActions == null ? null : java.util.List.copyOf(ruleActions);
        this.rulePriority = rulePriority;
        this.ruleStatus = ruleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRuleDefinition that = (StepRuleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(ruleType, that.ruleType) && Objects.equals(ruleCondition, that.ruleCondition) && Objects.equals(ruleActions, that.ruleActions) && rulePriority == that.rulePriority && Objects.equals(ruleStatus, that.ruleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ruleType, ruleCondition, ruleActions, rulePriority, ruleStatus);
    }

    @Override
    public String toString() {
        return "StepRuleDefinition{" + "id=" + id + "name=" + name + "ruleType=" + ruleType + "ruleCondition=" + ruleCondition + "ruleActions=" + ruleActions + "rulePriority=" + rulePriority + "ruleStatus=" + ruleStatus + "}";
    }
}