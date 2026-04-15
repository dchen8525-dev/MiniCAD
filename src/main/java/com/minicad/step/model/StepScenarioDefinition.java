package com.minicad.step.model;

import java.util.List;

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
public record StepScenarioDefinition(
    int id,
    String name,
    String varianceScenario,
    List<StepEntity> varianceSequence,
    List<StepEntity> varianceStates,
    List<StepEntity> varianceEvents,
    String varianceOutcome,
    String varianceStatus) implements StepEntity {
}