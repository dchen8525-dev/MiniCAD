package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSimulationModel implements StepEntity {
    private final int id;
    private final String name;
    private final String simulationType;
    private final StepEntity simulationGeometry;
    private final List<StepEntity> simulationParameters;
    private final List<StepEntity> initialConditions;
    private final double timeStep;
    private final double duration;

    public StepSimulationModel(int id, String name, String simulationType, StepEntity simulationGeometry, List<StepEntity> simulationParameters, List<StepEntity> initialConditions, double timeStep, double duration) {
        this.id = id;
        this.name = name;
        this.simulationType = simulationType;
        this.simulationGeometry = simulationGeometry;
        this.simulationParameters = simulationParameters == null ? null : java.util.List.copyOf(simulationParameters);
        this.initialConditions = initialConditions == null ? null : java.util.List.copyOf(initialConditions);
        this.timeStep = timeStep;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSimulationModel that = (StepSimulationModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(simulationType, that.simulationType) && Objects.equals(simulationGeometry, that.simulationGeometry) && Objects.equals(simulationParameters, that.simulationParameters) && Objects.equals(initialConditions, that.initialConditions) && timeStep == that.timeStep && duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, simulationType, simulationGeometry, simulationParameters, initialConditions, timeStep, duration);
    }

    @Override
    public String toString() {
        return "StepSimulationModel{" + "id=" + id + "name=" + name + "simulationType=" + simulationType + "simulationGeometry=" + simulationGeometry + "simulationParameters=" + simulationParameters + "initialConditions=" + initialConditions + "timeStep=" + timeStep + "duration=" + duration + "}";
    }
}