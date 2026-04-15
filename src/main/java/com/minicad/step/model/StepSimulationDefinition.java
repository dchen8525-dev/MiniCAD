package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SIMULATION_DEFINITION.
 * A simulation definition entity.
 *
 * @param id STEP instance id
 * @param name simulation name
 * @param simulationType simulation variance type
 * @param simulationModel simulation variance model reference
 * @param simulationParameters simulation variance parameters
 * @param simulationDuration simulation variance duration
 * @param simulationStatus simulation variance status
 */
public record StepSimulationDefinition(
    int id,
    String name,
    String simulationType,
    StepEntity simulationModel,
    List<String> simulationParameters,
    double simulationDuration,
    String simulationStatus) implements StepEntity {
}