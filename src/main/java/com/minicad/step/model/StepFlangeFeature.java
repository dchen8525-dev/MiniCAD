package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FLANGE_FEATURE.
 * A flange feature entity.
 *
 * @param id STEP instance id
 * @param name flange name
 * @param flangeType flange type classification (flat, raised face, weld neck)
 * @param flangeDiameter flange outer diameter
 * @param flangeThickness flange thickness
 * @param boltHoles bolt hole features
 * @param boltCircle bolt circle diameter
 * @param numberOfBoltHoles number of bolt holes
 * @param flangeStandard flange standard specification
 */
public record StepFlangeFeature(
    int id,
    String name,
    String flangeType,
    double flangeDiameter,
    double flangeThickness,
    List<StepEntity> boltHoles,
    double boltCircle,
    int numberOfBoltHoles,
    String flangeStandard) implements StepEntity {
}