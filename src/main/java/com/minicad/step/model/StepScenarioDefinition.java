package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SCENARIO_DEFINITION.
 * A scenario definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceScenario defined variance scenario
 * @varianceSequence scenario variance sequence/steps
 * @varianceStates scenario variance involved states
 * @varianceEvents scenario variance involved events
 * @varianceOutcome scenario variance expected outcome
 * @varianceStatus definition variance status
 */
public final class StepScenarioDefinition extends AbstractStepEntity {
    private final String varianceScenario;
    private final List<StepEntity> varianceSequence;
    private final List<StepEntity> varianceStates;
    private final List<StepEntity> varianceEvents;
    private final String varianceOutcome;
    private final String varianceStatus;

    public StepScenarioDefinition(int id, String name, String varianceScenario, List<StepEntity> varianceSequence, List<StepEntity> varianceStates, List<StepEntity> varianceEvents, String varianceOutcome, String varianceStatus) {
        super(id, name);
        this.varianceScenario = varianceScenario;
        this.varianceSequence = varianceSequence == null ? null : java.util.List.copyOf(varianceSequence);
        this.varianceStates = varianceStates == null ? null : java.util.List.copyOf(varianceStates);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceOutcome = varianceOutcome;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceScenario() {
        return varianceScenario;
    }

    public List<StepEntity> getVarianceSequence() {
        return varianceSequence;
    }

    public List<StepEntity> getVarianceStates() {
        return varianceStates;
    }

    public List<StepEntity> getVarianceEvents() {
        return varianceEvents;
    }

    public String getVarianceOutcome() {
        return varianceOutcome;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceScenario", varianceScenario);
        state.put("varianceSequence", varianceSequence);
        state.put("varianceStates", varianceStates);
        state.put("varianceEvents", varianceEvents);
        state.put("varianceOutcome", varianceOutcome);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
