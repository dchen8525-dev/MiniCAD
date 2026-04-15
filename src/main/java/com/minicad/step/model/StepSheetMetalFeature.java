package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SHEET_METAL_FEATURE.
 * A sheet metal feature entity.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType sheet metal feature type (flange, bend, cutout, hole)
 * @param sheetThickness sheet thickness
 * @param bendRadius bend radius for bends
 * @param bendAngle bend angle for bends
 * @param featureGeometry feature geometry representation
 * @param flatPattern flat pattern geometry reference
 */
public record StepSheetMetalFeature(
    int id,
    String name,
    String featureType,
    double sheetThickness,
    double bendRadius,
    double bendAngle,
    StepEntity featureGeometry,
    StepEntity flatPattern) implements StepEntity {
}