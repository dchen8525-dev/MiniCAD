package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OPTIMIZATION_CRITERIA.
 * An optimization criteria entity.
 *
 * @param id STEP instance id
 * @param name criteria name
 * @param objectiveType objective type (minimize, maximize)
 * @param objectiveVariable variable to optimize (weight, stress, displacement)
 * @param constraints optimization constraints
 * @param constraintValues constraint limit values
 * @param targetValue target objective value
 */
public record StepOptimizationCriteria(
    int id,
    String name,
    String objectiveType,
    String objectiveVariable,
    List<String> constraints,
    List<Double> constraintValues,
    double targetValue) implements StepEntity {
}