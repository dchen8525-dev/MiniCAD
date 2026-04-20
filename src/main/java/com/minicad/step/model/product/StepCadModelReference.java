package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CAD_MODEL_REFERENCE.
 * A CAD model reference entity.
 *
 * @param id STEP instance id
 * @param name reference name
 * @param modelId model identifier
 * @param modelType model type (3D, 2D, assembly)
 * @param modelGeometry model geometry reference
 * @param modelVersion model version reference
 * @param modelAuthor model author reference
 * @param modelStatus model status
 */
public record StepCadModelReference(
    int id,
    String name,
    String modelId,
    String modelType,
    StepEntity modelGeometry,
    StepEntity modelVersion,
    StepEntity modelAuthor,
    String modelStatus) implements StepEntity {
}