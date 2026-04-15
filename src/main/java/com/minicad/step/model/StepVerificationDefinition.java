package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VERIFICATION_DEFINITION.
 * A verification definition entity.
 *
 * @param id STEP instance id
 * @param name verification name
 * @param verificationType verification variance type
 * @param verificationMethod verification variance method
 * @param verificationCriteria verification variance criteria
 * @param verificationTolerance verification variance tolerance
 * @param verificationStatus verification variance status
 */
public record StepVerificationDefinition(
    int id,
    String name,
    String verificationType,
    String verificationMethod,
    List<String> verificationCriteria,
    double verificationTolerance,
    String verificationStatus) implements StepEntity {
}