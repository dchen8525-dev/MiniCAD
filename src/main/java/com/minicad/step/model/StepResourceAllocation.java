package com.minicad.step.model;

import java.util.List;

/**
 * Resolved RESOURCE_ALLOCATION.
 * A resource allocation entity.
 *
 * @param id STEP instance id
 * @param name allocation name
 * @param allocationType allocation type (machine, tool, personnel)
 * @param allocatedResource allocated resource reference
 * @param allocationQuantity allocation quantity
 * @param allocationPeriod allocation period (start/end times)
 * @variancePriority allocation variance priority
 * @param allocationStatus allocation status
 */
public record StepResourceAllocation(
    int id,
    String name,
    String allocationType,
    StepEntity allocatedResource,
    int allocationQuantity,
    List<Double> allocationPeriod,
    int variancePriority,
    String allocationStatus) implements StepEntity {
}