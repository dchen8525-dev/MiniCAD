package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SPOTFACE_HOLE_DEFINITION.
 * A spotface hole definition entity.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param throughHoleReference reference to the through hole
 * @param spotfaceDiameter diameter of the spotface
 * @param spotfaceDepth depth of the spotface
 */
public record StepSpotfaceHoleDefinition(
    int id,
    String name,
    StepEntity throughHoleReference,
    Double spotfaceDiameter,
    Double spotfaceDepth) implements StepEntity {
}