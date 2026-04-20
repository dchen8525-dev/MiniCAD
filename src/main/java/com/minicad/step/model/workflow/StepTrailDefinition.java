package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TRAIL_DEFINITION.
 * A trail definition entity.
 *
 * @param id STEP instance id
 * @param name trail name
 * @param trailType trail variance type
 * @param trailDescription trail variance description
 * @param trailEvents trail variance tracked events
 * @param trailRetention trail variance retention period
 * @param trailStatus trail variance status
 */
public record StepTrailDefinition(
    int id,
    String name,
    String trailType,
    String trailDescription,
    List<String> trailEvents,
    int trailRetention,
    String trailStatus) implements StepEntity {
}