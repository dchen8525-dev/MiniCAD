package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STRUCTURAL_FEATURE.
 * A structural feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param structuralType structural type (beam, column, plate, connection)
 * @param crossSection cross-section geometry
 * @param structuralLength length dimension
 * @param structuralMaterial material specification
 * @param endConditions end condition features
 * @param loadPoints load application points
 */
public record StepStructuralFeature(
    int id,
    String name,
    String structuralType,
    StepEntity crossSection,
    double structuralLength,
    StepEntity structuralMaterial,
    List<StepEntity> endConditions,
    List<StepEntity> loadPoints) implements StepEntity {
}