package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COUNTERSINK_HOLE.
 * Represents a countersink hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name countersink name
 * @param throughHole through hole reference
 * @param countersinkDiameter countersink diameter
 * @param countersinkAngle countersink angle
 */
public record StepCountersinkHole(
    int id,
    String name,
    StepEntity throughHole,
    Double countersinkDiameter,
    Double countersinkAngle) implements StepEntity {
}