package com.minicad.step.model;

/**
 * Resolved ATTRIBUTE_INSTANCE.
 * An attribute instance entity.
 *
 * @param id STEP instance id
 * @param name attribute instance name
 * @param attributeDefinition attribute variance definition reference
 * @param attributeValue attribute variance current value
 * @param attributeStatus attribute variance status
 */
public record StepAttributeInstance(
    int id,
    String name,
    StepEntity attributeDefinition,
    String attributeValue,
    String attributeStatus) implements StepEntity {
}