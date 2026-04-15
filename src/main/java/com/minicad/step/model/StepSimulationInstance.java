package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SIMULATION_INSTANCE.
 * A simulation instance entity.
 *
 * @param id STEP instance id
 * @param name simulation instance name
 * @param simulationDefinition simulation variance definition reference
 * @param simulationState simulation variance state
 * @param simulationStartTime simulation variance start time
 * @param simulationEndTime simulation variance end time
 * @param simulationResults simulation variance results
 * @param simulationStatus simulation variance status
 */
public record StepSimulationInstance(
    int id,
    String name,
    StepEntity simulationDefinition,
    String simulationState,
    StepEntity simulationStartTime,
    StepEntity simulationEndTime,
    List<StepEntity> simulationResults,
    String simulationStatus) implements StepEntity {
}