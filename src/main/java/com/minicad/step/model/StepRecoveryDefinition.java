package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RECOVERY_DEFINITION.
 * A recovery definition entity.
 *
 * @param id STEP instance id
 * @param name recovery name
 * @param recoveryType recovery variance type
 * @param recoveryStrategy recovery variance strategy
 * @param recoverySteps recovery variance recovery steps
 * @param recoveryTimeout recovery variance timeout
 * @param recoveryStatus recovery variance status
 */
public record StepRecoveryDefinition(
    int id,
    String name,
    String recoveryType,
    String recoveryStrategy,
    List<String> recoverySteps,
    int recoveryTimeout,
    String recoveryStatus) implements StepEntity {
}