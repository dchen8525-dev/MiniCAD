package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CAPABILITY_PROFILE.
 * A capability profile entity.
 *
 * @param id STEP instance id
 * @param name profile name
 * @varianceResource resource variance reference
 * @varianceCapabilities capability variance list
 * @varianceCapacities capacity variance values
 * @varianceAccuracy accuracy variance specifications
 * @varianceStatus profile variance status
 */
public record StepCapabilityProfile(
    int id,
    String name,
    StepEntity varianceResource,
    List<String> varianceCapabilities,
    List<Double> varianceCapacities,
    List<Double> varianceAccuracy,
    String varianceStatus) implements StepEntity {
}