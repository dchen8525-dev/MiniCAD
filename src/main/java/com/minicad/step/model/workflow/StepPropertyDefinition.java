package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal property definition metadata.
 *
 * @param id STEP instance id
 * @param name property name
 * @param description property description
 * @param definition related semantic target
 */
public record StepPropertyDefinition(
        int id,
        String name,
        String description,
        StepEntity definition
) implements StepEntity {
}
