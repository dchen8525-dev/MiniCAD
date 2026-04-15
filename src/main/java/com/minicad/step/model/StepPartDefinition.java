package com.minicad.step.model;

/**
 * Resolved PART_DEFINITION.
 * A part definition entity.
 *
 * @param id STEP instance id
 * @param name part name
 * @param partId part identifier
 * @param partType part type classification
 * @param geometryDefinition geometry definition reference
 * @param material material reference
 */
public record StepPartDefinition(
    int id,
    String name,
    String partId,
    String partType,
    StepEntity geometryDefinition,
    StepEntity material) implements StepEntity {
}