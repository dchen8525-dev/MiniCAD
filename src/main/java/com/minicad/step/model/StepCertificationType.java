package com.minicad.step.model;

/**
 * Minimal CERTIFICATION_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public record StepCertificationType(
        int id,
        String description
) implements StepEntity {

    @Override
    public String name() {
        return description;
    }
}
