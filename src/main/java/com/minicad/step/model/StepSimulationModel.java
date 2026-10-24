package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepSimulationModel extends AbstractStepEntity {
    private final String simulationType;
    private final StepEntity simulationGeometry;
    private final List<StepEntity> simulationParameters;
    private final List<StepEntity> initialConditions;
    private final double timeStep;
    private final double duration;

    public StepSimulationModel(int id, String name, String simulationType, StepEntity simulationGeometry, List<StepEntity> simulationParameters, List<StepEntity> initialConditions, double timeStep, double duration) {
        super(id, name);
        this.simulationType = simulationType;
        this.simulationGeometry = simulationGeometry;
        this.simulationParameters = simulationParameters == null ? null : java.util.List.copyOf(simulationParameters);
        this.initialConditions = initialConditions == null ? null : java.util.List.copyOf(initialConditions);
        this.timeStep = timeStep;
        this.duration = duration;
    }

    public String getSimulationType() {
        return simulationType;
    }

    public StepEntity getSimulationGeometry() {
        return simulationGeometry;
    }

    public List<StepEntity> getSimulationParameters() {
        return simulationParameters;
    }

    public List<StepEntity> getInitialConditions() {
        return initialConditions;
    }

    public double getTimeStep() {
        return timeStep;
    }

    public double getDuration() {
        return duration;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "simulationType", simulationType,
                "simulationGeometry", simulationGeometry, "simulationParameters", simulationParameters,
                "initialConditions", initialConditions, "timeStep", timeStep, "duration", duration);
    }
}
