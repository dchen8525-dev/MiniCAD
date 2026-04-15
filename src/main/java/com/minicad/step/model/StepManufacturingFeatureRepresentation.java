package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MANUFACTURING_FEATURE_REPRESENTATION.
 * Represents the representation of a manufacturing feature.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * @param context representation context
 */
public record StepManufacturingFeatureRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context) implements StepEntity {
}