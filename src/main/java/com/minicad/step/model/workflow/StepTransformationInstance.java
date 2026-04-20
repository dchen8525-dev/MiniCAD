package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRANSFORMATION_INSTANCE.
 * A transformation instance entity.
 *
 * @param id STEP instance id
 * @param name transformation instance name
 * @param transformationDefinition transformation variance definition reference
 * @param transformationState transformation variance state
 * @param transformationInputData transformation variance input data
 * @param transformationOutputData transformation variance output data
 * @param transformationStatus transformation variance status
 */
public record StepTransformationInstance(
    int id,
    String name,
    StepEntity transformationDefinition,
    String transformationState,
    List<String> transformationInputData,
    List<String> transformationOutputData,
    String transformationStatus) implements StepEntity {
}