package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRANSFORMATION_DEFINITION.
 * A transformation definition entity.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param transformationType transformation variance type
 * @param transformationInput transformation variance input type
 * @param transformationOutput transformation variance output type
 * @param transformationParameters transformation variance parameters
 * @param transformationStatus transformation variance status
 */
public record StepTransformationDefinition(
    int id,
    String name,
    String transformationType,
    String transformationInput,
    String transformationOutput,
    List<String> transformationParameters,
    String transformationStatus) implements StepEntity {
}