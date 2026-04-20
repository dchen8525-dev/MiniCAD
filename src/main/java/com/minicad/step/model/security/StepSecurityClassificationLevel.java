package com.minicad.step.model.security;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal SECURITY_CLASSIFICATION_LEVEL metadata.
 *
 * @param id STEP instance id
 * @param name level label
 */
public record StepSecurityClassificationLevel(
        int id,
        String name
) implements StepEntity {
}
