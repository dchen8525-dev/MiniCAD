package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal IDENTIFICATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepIdentificationRole(
        int id,
        String name
) implements StepEntity {
}
