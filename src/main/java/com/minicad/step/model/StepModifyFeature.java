package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MODIFY_FEATURE.
 * A modify feature entity for feature modifications.
 *
 * @param id STEP instance id
 * @param name modification name
 * @param originalFeature original feature being modified
 * @param modificationType modification type classification
 * @param modificationParameters modification parameters
 * @param modifiedGeometry modified geometry result
 */
public record StepModifyFeature(
    int id,
    String name,
    StepEntity originalFeature,
    String modificationType,
    List<StepEntity> modificationParameters,
    StepEntity modifiedGeometry) implements StepEntity {
}