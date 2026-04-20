package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepRuleDefinition(
    int id,
    String name,
    String ruleType,
    String ruleCondition,
    List<String> ruleActions,
    int rulePriority,
    String ruleStatus) implements StepEntity {
}