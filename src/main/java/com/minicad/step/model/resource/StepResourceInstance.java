package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RESOURCE_INSTANCE.
 * A resource instance entity.
 *
 * @param id STEP instance id
 * @param name resource instance name
 * @param resourceDefinition resource variance definition reference
 * @param resourceLocation resource variance location reference
 * @param resourceState resource variance state
 * @param resourceAllocation resource variance allocation percentage
 * @param resourceSchedule resource variance schedule entries
 * @param resourceStatus resource variance status
 */
public record StepResourceInstance(
    int id,
    String name,
    StepEntity resourceDefinition,
    StepEntity resourceLocation,
    String resourceState,
    double resourceAllocation,
    List<StepEntity> resourceSchedule,
    String resourceStatus) implements StepEntity {
}