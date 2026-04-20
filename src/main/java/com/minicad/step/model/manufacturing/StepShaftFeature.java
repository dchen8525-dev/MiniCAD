package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SHAFT_FEATURE.
 * A shaft feature entity.
 *
 * @param id STEP instance id
 * @param name shaft name
 * @param shaftDiameter shaft diameter
 * @param shaftLength shaft length
 * @param shaftType shaft type classification (solid, hollow, stepped)
 * @param features features on the shaft (keyways, threads, grooves)
 * @param shaftMaterial shaft material specification
 * @param surfaceTreatment surface treatment specification
 */
public record StepShaftFeature(
    int id,
    String name,
    double shaftDiameter,
    double shaftLength,
    String shaftType,
    List<StepEntity> features,
    StepEntity shaftMaterial,
    StepEntity surfaceTreatment) implements StepEntity {
}