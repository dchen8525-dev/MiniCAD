package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved CAPABILITY_INSTANCE.
 * A capability instance entity.
 *
 * @param id STEP instance id
 * @param name capability instance name
 * @param capabilityDefinition capability variance definition reference
 * @param capabilityState capability variance state
 * @param capabilityScore capability variance score
 * @param capabilityHistory capability variance history records
 * @param capabilityStatus capability variance status
 */
public record StepCapabilityInstance(
    int id,
    String name,
    StepEntity capabilityDefinition,
    String capabilityState,
    double capabilityScore,
    List<String> capabilityHistory,
    String capabilityStatus) implements StepEntity {
}