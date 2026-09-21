package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSimulationDefinition extends AbstractStepEntity {
    private final String simulationType;
    private final StepEntity simulationModel;
    private final List<String> simulationParameters;
    private final double simulationDuration;
    private final String simulationStatus;

    public StepSimulationDefinition(int id, String name, String simulationType, StepEntity simulationModel, List<String> simulationParameters, double simulationDuration, String simulationStatus) {
        super(id, name);
        this.simulationType = simulationType;
        this.simulationModel = simulationModel;
        this.simulationParameters = simulationParameters == null ? null : java.util.List.copyOf(simulationParameters);
        this.simulationDuration = simulationDuration;
        this.simulationStatus = simulationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("simulationType", simulationType);
        state.put("simulationModel", simulationModel);
        state.put("simulationParameters", simulationParameters);
        state.put("simulationDuration", simulationDuration);
        state.put("simulationStatus", simulationStatus);
        return state;
    }
}
