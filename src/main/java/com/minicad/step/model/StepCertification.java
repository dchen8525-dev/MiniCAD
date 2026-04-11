package com.minicad.step.model;

/**
 * Minimal CERTIFICATION metadata.
 *
 * @param id STEP instance id
 * @param name certification name
 * @param purpose certification purpose
 * @param kind certification type
 */
public record StepCertification(
        int id,
        String name,
        String purpose,
        StepCertificationType kind
) implements StepEntity {
}
