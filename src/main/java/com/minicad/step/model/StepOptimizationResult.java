package com.minicad.step.model;

import java.util.List;

/**
 * Resolved OPTIMIZATION_RESULT.
 * An optimization result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param optimizedGeometry optimized geometry result
 * @param optimizedVariables optimized variable values
 * @param objectiveValue achieved objective value
 * @param iterationCount number of optimization iterations
 * @param convergenceStatus convergence status (converged, not converged)
 * @param constraintsMet constraints satisfaction status
 */
public record StepOptimizationResult(
    int id,
    String name,
    StepEntity optimizedGeometry,
    List<Double> optimizedVariables,
    double objectiveValue,
    int iterationCount,
    String convergenceStatus,
    List<Boolean> constraintsMet) implements StepEntity {
}