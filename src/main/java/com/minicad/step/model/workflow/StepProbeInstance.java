package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROBE_INSTANCE.
 * A probe instance entity.
 *
 * @param id STEP instance id
 * @param name probe instance name
 * @param probeDefinition probe variance definition reference
 * @param probeState probe variance state
 * @param probeLastProbe probe variance last probe time
 * @param probeResult probe variance result
 * @param probeStatus probe variance status
 */
public record StepProbeInstance(
    int id,
    String name,
    StepEntity probeDefinition,
    String probeState,
    StepEntity probeLastProbe,
    String probeResult,
    String probeStatus) implements StepEntity {
}