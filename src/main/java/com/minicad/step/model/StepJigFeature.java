package com.minicad.step.model;

import java.util.List;

/**
 * Resolved JIG_FEATURE.
 * A jig feature entity.
 *
 * @param id STEP instance id
 * @param name jig name
 * @param jigType jig type classification
 * @param jigGeometry jig geometry representation
 * @param guideElements guide elements for tool positioning
 * @param referenceSurfaces reference surfaces for alignment
 * @param jigCapacity jig capacity/workpiece size
 * @param jigMaterial jig material reference
 */
public record StepJigFeature(
    int id,
    String name,
    String jigType,
    StepEntity jigGeometry,
    List<StepEntity> guideElements,
    List<StepEntity> referenceSurfaces,
    double jigCapacity,
    StepEntity jigMaterial) implements StepEntity {
}