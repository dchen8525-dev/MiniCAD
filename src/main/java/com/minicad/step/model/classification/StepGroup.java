package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal GROUP metadata.
 *
 * @param id STEP instance id
 * @param name group name
 * @param description group description
 * @param entityName concrete STEP entity name
 */
public record StepGroup(
        int id,
        String name,
        String description,
        String entityName
) implements StepEntity {
}
