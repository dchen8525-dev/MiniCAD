package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SIMULATION_RESULT.
 * A simulation result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceModel simulation variance model reference
 * @varianceScenario simulation variance scenario
 * @varianceOutputs simulation variance output values
 * @varianceTime simulation variance time steps
 * @varianceConvergence convergence variance status
 * @varianceStatus result variance status
 */
public record StepSimulationResult(
    int id,
    String name,
    StepEntity varianceModel,
    StepEntity varianceScenario,
    List<Double> varianceOutputs,
    List<Double> varianceTime,
    boolean varianceConvergence,
    String varianceStatus) implements StepEntity {
}