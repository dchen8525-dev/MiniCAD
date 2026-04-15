package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MAPPING_INSTANCE.
 * A mapping instance entity.
 *
 * @param id STEP instance id
 * @param name mapping instance name
 * @param mappingDefinition mapping variance definition reference
 * @param mappingInput mapping variance input data
 * @param mappingOutput mapping variance output data
 * @param mappingStatus mapping variance status
 */
public record StepMappingInstance(
    int id,
    String name,
    StepEntity mappingDefinition,
    List<String> mappingInput,
    List<String> mappingOutput,
    String mappingStatus) implements StepEntity {
}