package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONNECTION_DEFINITION.
 * A connection definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceConnection defined variance connection
 * @varianceFrom source variance component
 * @varianceTo target variance component
 * @varianceType connection variance type
 * @varianceInterface connection variance interface specification
 * @varianceStatus definition variance status
 */
public record StepConnectionDefinition(
    int id,
    String name,
    StepEntity varianceConnection,
    StepEntity varianceFrom,
    StepEntity varianceTo,
    String varianceType,
    StepEntity varianceInterface,
    String varianceStatus) implements StepEntity {
}