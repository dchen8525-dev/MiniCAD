package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepRuleInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity ruleDefinition;
    private final String ruleState;
    private final boolean ruleResult;
    private final int ruleApplicationCount;
    private final String ruleStatus;

    public StepRuleInstance(int id, String name, StepEntity ruleDefinition, String ruleState, boolean ruleResult, int ruleApplicationCount, String ruleStatus) {
        this.id = id;
        this.name = name;
        this.ruleDefinition = ruleDefinition;
        this.ruleState = ruleState;
        this.ruleResult = ruleResult;
        this.ruleApplicationCount = ruleApplicationCount;
        this.ruleStatus = ruleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRuleInstance that = (StepRuleInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(ruleDefinition, that.ruleDefinition) && Objects.equals(ruleState, that.ruleState) && ruleResult == that.ruleResult && ruleApplicationCount == that.ruleApplicationCount && Objects.equals(ruleStatus, that.ruleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ruleDefinition, ruleState, ruleResult, ruleApplicationCount, ruleStatus);
    }

    @Override
    public String toString() {
        return "StepRuleInstance{" + "id=" + id + "name=" + name + "ruleDefinition=" + ruleDefinition + "ruleState=" + ruleState + "ruleResult=" + ruleResult + "ruleApplicationCount=" + ruleApplicationCount + "ruleStatus=" + ruleStatus + "}";
    }
}