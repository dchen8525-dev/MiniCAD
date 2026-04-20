package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SEQUENCE_DEFINITION.
 * A sequence definition entity.
 *
 * @param id STEP instance id
 * @param name sequence name
 * @param sequenceType sequence variance type
 * @param sequenceItems sequence variance item definitions
 * @param sequenceOrder sequence variance ordering policy
 * @param sequenceStatus sequence variance status
 */
public record StepSequenceDefinition(
    int id,
    String name,
    String sequenceType,
    List<StepEntity> sequenceItems,
    String sequenceOrder,
    String sequenceStatus) implements StepEntity {
}