package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VERIFICATION_INSTANCE.
 * A verification instance entity.
 *
 * @param id STEP instance id
 * @param name verification instance name
 * @param verificationDefinition verification variance definition reference
 * @param verificationTarget verification variance target reference
 * @param verificationResult verification variance result (passed/failed)
 * @param verificationMeasurements verification variance measurements
 * @param verificationStatus verification variance status
 */
public record StepVerificationInstance(
    int id,
    String name,
    StepEntity verificationDefinition,
    StepEntity verificationTarget,
    boolean verificationResult,
    List<Double> verificationMeasurements,
    String verificationStatus) implements StepEntity {
}