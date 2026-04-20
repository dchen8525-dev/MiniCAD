package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MODEL_INSTANCE.
 * A model instance entity.
 *
 * @param id STEP instance id
 * @param name model instance name
 * @param modelDefinition model variance definition reference
 * @param modelState model variance state
 * @param modelVersion model variance version
 * @param modelProperties model variance properties
 * @param modelStatus model variance status
 */
public record StepModelInstance(
    int id,
    String name,
    StepEntity modelDefinition,
    String modelState,
    String modelVersion,
    List<String> modelProperties,
    String modelStatus) implements StepEntity {
}