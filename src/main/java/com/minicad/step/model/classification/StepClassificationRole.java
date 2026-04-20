package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal CLASSIFICATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepClassificationRole(
        int id,
        String name
) implements StepEntity {
}
