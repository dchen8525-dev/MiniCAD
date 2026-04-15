package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TOLERANCE_PAIR.
 * A tolerance pair entity (limits and fits).
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperTolerance upper tolerance value
 * @param lowerTolerance lower tolerance value
 * * @param toleranceUnit tolerance unit
 * @param fitType fit type classification
 */
public record StepTolerancePair(
    int id,
    String name,
    Double upperTolerance,
    Double lowerTolerance,
    StepEntity toleranceUnit,
    String fitType) implements StepEntity {
}