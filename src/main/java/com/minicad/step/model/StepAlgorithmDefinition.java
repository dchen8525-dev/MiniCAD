package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ALGORITHM_DEFINITION.
 * An algorithm definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceAlgorithm defined variance algorithm
 * @varianceInputs algorithm variance inputs
 * @varianceOutputs algorithm variance outputs
 * @varianceSteps algorithm variance steps/procedure
 * @varianceComplexity algorithm variance complexity level
 * @varianceStatus definition variance status
 */
public record StepAlgorithmDefinition(
    int id,
    String name,
    String varianceAlgorithm,
    List<StepEntity> varianceInputs,
    List<StepEntity> varianceOutputs,
    List<String> varianceSteps,
    int varianceComplexity,
    String varianceStatus) implements StepEntity {
}