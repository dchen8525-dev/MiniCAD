package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved EVENT_DEFINITION.
 * An event definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceEvent defined variance event
 * @varianceType event variance type (internal, external, time)
 * @varianceTrigger event variance trigger condition
 * @varianceResponse event variance response action
 * @variancePriority event variance priority
 * @varianceStatus definition variance status
 */
public record StepEventDefinition(
    int id,
    String name,
    String varianceEvent,
    String varianceType,
    String varianceTrigger,
    StepEntity varianceResponse,
    int variancePriority,
    String varianceStatus) implements StepEntity {
}