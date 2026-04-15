package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PROBE_DEFINITION.
 * A probe definition entity.
 *
 * @param id STEP instance id
 * @param name probe name
 * @param probeType probe variance type
 * @param probeTarget probe variance target reference
 * @param probeParameters probe variance parameters
 * @param probeInterval probe variance probe interval
 * @param probeStatus probe variance status
 */
public record StepProbeDefinition(
    int id,
    String name,
    String probeType,
    StepEntity probeTarget,
    List<String> probeParameters,
    int probeInterval,
    String probeStatus) implements StepEntity {
}