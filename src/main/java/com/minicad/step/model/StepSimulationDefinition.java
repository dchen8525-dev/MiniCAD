package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSimulationDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String simulationType;
    private final StepEntity simulationModel;
    private final List<String> simulationParameters;
    private final double simulationDuration;
    private final String simulationStatus;

    public StepSimulationDefinition(int id, String name, String simulationType, StepEntity simulationModel, List<String> simulationParameters, double simulationDuration, String simulationStatus) {
        this.id = id;
        this.name = name;
        this.simulationType = simulationType;
        this.simulationModel = simulationModel;
        this.simulationParameters = simulationParameters == null ? null : java.util.List.copyOf(simulationParameters);
        this.simulationDuration = simulationDuration;
        this.simulationStatus = simulationStatus;
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

    public StepEntity getSimulationModel() {
        return simulationModel;
    }

    public List<String> getSimulationParameters() {
        return simulationParameters;
    }

    public double getSimulationDuration() {
        return simulationDuration;
    }

    public String getSimulationStatus() {
        return simulationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSimulationDefinition that = (StepSimulationDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(simulationType, that.simulationType) && Objects.equals(simulationModel, that.simulationModel) && Objects.equals(simulationParameters, that.simulationParameters) && simulationDuration == that.simulationDuration && Objects.equals(simulationStatus, that.simulationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, simulationType, simulationModel, simulationParameters, simulationDuration, simulationStatus);
    }

    @Override
    public String toString() {
        return "StepSimulationDefinition{" + "id=" + id + "name=" + name + "simulationType=" + simulationType + "simulationModel=" + simulationModel + "simulationParameters=" + simulationParameters + "simulationDuration=" + simulationDuration + "simulationStatus=" + simulationStatus + "}";
    }
}