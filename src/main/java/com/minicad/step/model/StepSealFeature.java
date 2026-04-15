package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SEAL_FEATURE.
 * A seal feature entity.
 *
 * @param id STEP instance id
 * @param name seal name
 * @param sealType seal type classification (O-ring, gasket, lip seal)
 * @param innerDiameter inner diameter
 * @param outerDiameter outer diameter
 * @param sealWidth seal width/cross-section
 * @param sealMaterial seal material specification
 * @param sealPlacement seal position placement
 */
public record StepSealFeature(
    int id,
    String name,
    String sealType,
    double innerDiameter,
    double outerDiameter,
    double sealWidth,
    StepEntity sealMaterial,
    StepEntity sealPlacement) implements StepEntity {
}