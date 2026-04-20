package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SIMULATION_MODEL.
 * A simulation model entity.
 *
 * @param id STEP instance id
 * @param name model name
 * @param simulationType simulation type (kinematic, dynamic, thermal)
 * @param simulationGeometry geometry for simulation
 * @param simulationParameters simulation parameters
 * @param initialConditions initial conditions
 * @param timeStep time step for transient simulation
 * @param duration simulation duration
 */
public record StepSimulationModel(
    int id,
    String name,
    String simulationType,
    StepEntity simulationGeometry,
    List<StepEntity> simulationParameters,
    List<StepEntity> initialConditions,
    double timeStep,
    double duration) implements StepEntity {
}