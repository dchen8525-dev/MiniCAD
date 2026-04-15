package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MAPPING_DEFINITION.
 * A mapping definition entity.
 *
 * @param id STEP instance id
 * @param name mapping name
 * @param mappingType mapping variance type
 * @param mappingSource mapping variance source domain
 * @param mappingTarget mapping variance target domain
 * @param mappingRules mapping variance mapping rules
 * @param mappingStatus mapping variance status
 */
public record StepMappingDefinition(
    int id,
    String name,
    String mappingType,
    String mappingSource,
    String mappingTarget,
    List<String> mappingRules,
    String mappingStatus) implements StepEntity {
}