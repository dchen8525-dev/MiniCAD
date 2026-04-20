package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal GENERAL_PROPERTY metadata.
 *
 * @param id STEP instance id
 * @param propertyId property identifier
 * @param name property name
 * @param description property description
 */
public record StepGeneralProperty(
        int id,
        String propertyId,
        String name,
        String description
) implements StepEntity {
}
