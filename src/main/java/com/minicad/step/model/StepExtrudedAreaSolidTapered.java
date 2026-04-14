package com.minicad.step.model;

/**
 * Resolved EXTRUDED_AREA_SOLID_TAPERED.
 * An extruded solid with tapered profile.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to extrude
 * @param direction extrusion direction
 * @param depth extrusion depth
 * @param taperAngle taper angle
 */
public record StepExtrudedAreaSolidTapered(
    int id,
    String name,
    StepEntity sweptArea,
    StepDirection direction,
    double depth,
    double taperAngle) implements StepEntity {
}
