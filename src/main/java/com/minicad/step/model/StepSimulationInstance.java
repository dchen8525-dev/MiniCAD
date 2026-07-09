package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSimulationInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity simulationDefinition;
    private final String simulationState;
    private final StepEntity simulationStartTime;
    private final StepEntity simulationEndTime;
    private final List<StepEntity> simulationResults;
    private final String simulationStatus;

    public StepSimulationInstance(int id, String name, StepEntity simulationDefinition, String simulationState, StepEntity simulationStartTime, StepEntity simulationEndTime, List<StepEntity> simulationResults, String simulationStatus) {
        this.id = id;
        this.name = name;
        this.simulationDefinition = simulationDefinition;
        this.simulationState = simulationState;
        this.simulationStartTime = simulationStartTime;
        this.simulationEndTime = simulationEndTime;
        this.simulationResults = simulationResults == null ? null : java.util.List.copyOf(simulationResults);
        this.simulationStatus = simulationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSimulationDefinition() {
        return simulationDefinition;
    }

    public String getSimulationState() {
        return simulationState;
    }

    public StepEntity getSimulationStartTime() {
        return simulationStartTime;
    }

    public StepEntity getSimulationEndTime() {
        return simulationEndTime;
    }

    public List<StepEntity> getSimulationResults() {
        return simulationResults;
    }

    public String getSimulationStatus() {
        return simulationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSimulationInstance that = (StepSimulationInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(simulationDefinition, that.simulationDefinition) && Objects.equals(simulationState, that.simulationState) && Objects.equals(simulationStartTime, that.simulationStartTime) && Objects.equals(simulationEndTime, that.simulationEndTime) && Objects.equals(simulationResults, that.simulationResults) && Objects.equals(simulationStatus, that.simulationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, simulationDefinition, simulationState, simulationStartTime, simulationEndTime, simulationResults, simulationStatus);
    }

    @Override
    public String toString() {
        return "StepSimulationInstance{" + "id=" + id + "name=" + name + "simulationDefinition=" + simulationDefinition + "simulationState=" + simulationState + "simulationStartTime=" + simulationStartTime + "simulationEndTime=" + simulationEndTime + "simulationResults=" + simulationResults + "simulationStatus=" + simulationStatus + "}";
    }
}