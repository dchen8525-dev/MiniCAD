package com.minicad.step.model;

import java.util.List;

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
public record StepPolicyInstance(
    int id,
    String name,
    StepEntity policyDefinition,
    String policyState,
    int policyViolations,
    int policyExceptions,
    String policyStatus) implements StepEntity {
}