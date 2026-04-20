package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
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
