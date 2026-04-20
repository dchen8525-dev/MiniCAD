package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MODEL_DEFINITION.
 * A model definition entity.
 *
 * @param id STEP instance id
 * @param name model name
 * @param modelType model variance type
 * @param modelGeometry model variance geometry reference
 * @param modelParameters model variance parameters
 * @param modelConstraints model variance constraints
 * @param modelStatus model variance status
 */
public record StepModelDefinition(
    int id,
    String name,
    String modelType,
    StepEntity modelGeometry,
    List<String> modelParameters,
    List<String> modelConstraints,
    String modelStatus) implements StepEntity {
}