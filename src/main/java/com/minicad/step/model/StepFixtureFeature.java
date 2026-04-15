package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FIXTURE_FEATURE.
 * A fixture feature entity.
 *
 * @param id STEP instance id
 * @param name fixture name
 * @param fixtureType fixture type (clamping, supporting, locating)
 * @param fixtureGeometry fixture geometry representation
 * @param clampingPoints clamping point locations
 * @param supportingPoints supporting point locations
 * @param fixtureForce fixture force specification
 * @param fixtureMaterial fixture material reference
 */
public record StepFixtureFeature(
    int id,
    String name,
    String fixtureType,
    StepEntity fixtureGeometry,
    List<StepEntity> clampingPoints,
    List<StepEntity> supportingPoints,
    double fixtureForce,
    StepEntity fixtureMaterial) implements StepEntity {
}