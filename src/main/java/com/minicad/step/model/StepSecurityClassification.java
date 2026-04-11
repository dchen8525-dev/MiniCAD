package com.minicad.step.model;

/**
 * Minimal SECURITY_CLASSIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name classification name
 * @param purpose classification purpose
 * @param securityLevel classification level
 */
public record StepSecurityClassification(
        int id,
        String name,
        String purpose,
        StepSecurityClassificationLevel securityLevel
) implements StepEntity {
}
