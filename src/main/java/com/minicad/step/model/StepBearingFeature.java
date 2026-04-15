package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BEARING_FEATURE.
 * A bearing feature entity.
 *
 * @param id STEP instance id
 * @param name bearing name
 * @param bearingType bearing type classification (ball, roller, needle, plain)
 * @param boreDiameter bore (inner) diameter
 * @param outerDiameter outer diameter
 * @param bearingWidth bearing width
 * @param numberOfElements number of bearing elements (balls, rollers)
 * @param bearingStandard bearing standard specification
 * @param bearingPlacement bearing position placement
 */
public record StepBearingFeature(
    int id,
    String name,
    String bearingType,
    double boreDiameter,
    double outerDiameter,
    double bearingWidth,
    int numberOfElements,
    String bearingStandard,
    StepEntity bearingPlacement) implements StepEntity {
}