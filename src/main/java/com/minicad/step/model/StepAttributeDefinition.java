package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ATTRIBUTE_DEFINITION.
 * An attribute definition entity.
 *
 * @param id STEP instance id
 * @param name attribute name
 * @param attributeType attribute variance type
 * @param attributeDataType attribute variance data type
 * @param attributeRange attribute variance valid range
 * @param attributeDefault attribute variance default value
 * @param attributeStatus attribute variance status
 */
public record StepAttributeDefinition(
    int id,
    String name,
    String attributeType,
    String attributeDataType,
    List<String> attributeRange,
    String attributeDefault,
    String attributeStatus) implements StepEntity {
}