package com.minicad.step.model;

import java.util.List;

/**
 * Resolved POLICY_DEFINITION.
 * A policy definition entity.
 *
 * @param id STEP instance id
 * @param name policy name
 * @param policyType policy variance type
 * @param policyScope policy variance scope
 * @param policyRules policy variance rules
 * @param policyEnforcement policy variance enforcement level
 * @param policyStatus policy variance status
 */
public record StepPolicyDefinition(
    int id,
    String name,
    String policyType,
    String policyScope,
    List<String> policyRules,
    String policyEnforcement,
    String policyStatus) implements StepEntity {
}