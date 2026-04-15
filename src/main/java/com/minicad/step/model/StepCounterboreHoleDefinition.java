package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COUNTERBORE_HOLE_DEFINITION.
 * A counterbore hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param counterboreDiameter diameter of the counterbore
 * @param counterboreDepth depth of the counterbore
 */
public record StepCounterboreHoleDefinition(
    int id,
    String name,
    StepEntity throughHoleReference,
    Double counterboreDiameter,
    Double counterboreDepth) implements StepEntity {
}