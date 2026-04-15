package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DESIGN_VERIFICATION.
 * A design verification entity.
 *
 * @param id STEP instance id
 * @param name verification name
 * @param verificationType verification type (calculation, simulation, review)
 * @param verificationCriteria verification criteria reference
 * @param verificationResults verification results
 * @param verificationStatus verification status (verified, not verified)
 * @param verificationMethod verification method description
 * @param verificationEvidence verification evidence reference
 */
public record StepDesignVerification(
    int id,
    String name,
    String verificationType,
    StepEntity verificationCriteria,
    List<StepEntity> verificationResults,
    String verificationStatus,
    String verificationMethod,
    StepEntity verificationEvidence) implements StepEntity {
}