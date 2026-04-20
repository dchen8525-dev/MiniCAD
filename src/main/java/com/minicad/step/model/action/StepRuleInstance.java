package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepRuleInstance(
    int id,
    String name,
    StepEntity ruleDefinition,
    String ruleState,
    boolean ruleResult,
    int ruleApplicationCount,
    String ruleStatus) implements StepEntity {
}