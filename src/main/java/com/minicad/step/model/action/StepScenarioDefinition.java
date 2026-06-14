package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepScenarioDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceScenario;
    private final List<StepEntity> varianceSequence;
    private final List<StepEntity> varianceStates;
    private final List<StepEntity> varianceEvents;
    private final String varianceOutcome;
    private final String varianceStatus;

    public StepScenarioDefinition(int id, String name, String varianceScenario, List<StepEntity> varianceSequence, List<StepEntity> varianceStates, List<StepEntity> varianceEvents, String varianceOutcome, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceScenario = varianceScenario;
        this.varianceSequence = varianceSequence == null ? null : java.util.List.copyOf(varianceSequence);
        this.varianceStates = varianceStates == null ? null : java.util.List.copyOf(varianceStates);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceOutcome = varianceOutcome;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepScenarioDefinition that = (StepScenarioDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceScenario, that.varianceScenario) && Objects.equals(varianceSequence, that.varianceSequence) && Objects.equals(varianceStates, that.varianceStates) && Objects.equals(varianceEvents, that.varianceEvents) && Objects.equals(varianceOutcome, that.varianceOutcome) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceScenario, varianceSequence, varianceStates, varianceEvents, varianceOutcome, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepScenarioDefinition{" + "id=" + id + "name=" + name + "varianceScenario=" + varianceScenario + "varianceSequence=" + varianceSequence + "varianceStates=" + varianceStates + "varianceEvents=" + varianceEvents + "varianceOutcome=" + varianceOutcome + "varianceStatus=" + varianceStatus + "}";
    }
}