package com.minicad.step.model;

import java.util.List;

/**
 * Resolved GEAR_FEATURE.
 * A gear feature entity.
 *
 * @param id STEP instance id
 * @param name gear name
 * @param gearType gear type classification (spur, helical, bevel, worm)
 * @param numberOfTeeth number of gear teeth
 * @param module gear module
 * @param pressureAngle pressure angle in degrees
 * @param helixAngle helix angle for helical gears
 * @param pitchDiameter pitch diameter
 * @param rootDiameter root diameter
 * @param tipDiameter tip diameter
 */
public record StepGearFeature(
    int id,
    String name,
    String gearType,
    int numberOfTeeth,
    double module,
    double pressureAngle,
    double helixAngle,
    double pitchDiameter,
    double rootDiameter,
    double tipDiameter) implements StepEntity {
}