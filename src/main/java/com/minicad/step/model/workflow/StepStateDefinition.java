package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STATE_DEFINITION.
 * A state definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceState defined variance state
 * @varianceConditions state variance conditions
 * @varianceActions state variance actions
 * @varianceTransitions state variance transitions
 * @varianceInitial initial variance state flag
 * @varianceStatus definition variance status
 */
public record StepStateDefinition(
    int id,
    String name,
    String varianceState,
    List<String> varianceConditions,
    List<StepEntity> varianceActions,
    List<StepEntity> varianceTransitions,
    boolean varianceInitial,
    String varianceStatus) implements StepEntity {
}