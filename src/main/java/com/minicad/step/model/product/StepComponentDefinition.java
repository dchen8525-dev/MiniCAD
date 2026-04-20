package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPONENT_DEFINITION.
 * A component definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceComponent defined variance component
 * @varianceFunction component variance function
 * @varianceInterface component variance interface specification
 * @varianceDependencies component variance dependencies
 * @varianceProperties component variance properties
 * @varianceStatus definition variance status
 */
public record StepComponentDefinition(
    int id,
    String name,
    StepEntity varianceComponent,
    String varianceFunction,
    StepEntity varianceInterface,
    List<StepEntity> varianceDependencies,
    List<StepEntity> varianceProperties,
    String varianceStatus) implements StepEntity {
}