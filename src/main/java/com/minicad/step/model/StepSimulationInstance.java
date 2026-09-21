package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSimulationInstance extends AbstractStepEntity {
    private final StepEntity simulationDefinition;
    private final String simulationState;
    private final StepEntity simulationStartTime;
    private final StepEntity simulationEndTime;
    private final List<StepEntity> simulationResults;
    private final String simulationStatus;

    public StepSimulationInstance(int id, String name, StepEntity simulationDefinition, String simulationState, StepEntity simulationStartTime, StepEntity simulationEndTime, List<StepEntity> simulationResults, String simulationStatus) {
        super(id, name);
        this.simulationDefinition = simulationDefinition;
        this.simulationState = simulationState;
        this.simulationStartTime = simulationStartTime;
        this.simulationEndTime = simulationEndTime;
        this.simulationResults = simulationResults == null ? null : java.util.List.copyOf(simulationResults);
        this.simulationStatus = simulationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("simulationDefinition", simulationDefinition);
        state.put("simulationState", simulationState);
        state.put("simulationStartTime", simulationStartTime);
        state.put("simulationEndTime", simulationEndTime);
        state.put("simulationResults", simulationResults);
        state.put("simulationStatus", simulationStatus);
        return state;
    }
}
