package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepProbeInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity probeDefinition;
    private final String probeState;
    private final StepEntity probeLastProbe;
    private final String probeResult;
    private final String probeStatus;

    public StepProbeInstance(int id, String name, StepEntity probeDefinition, String probeState, StepEntity probeLastProbe, String probeResult, String probeStatus) {
        this.id = id;
        this.name = name;
        this.probeDefinition = probeDefinition;
        this.probeState = probeState;
        this.probeLastProbe = probeLastProbe;
        this.probeResult = probeResult;
        this.probeStatus = probeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getProbeDefinition() {
        return probeDefinition;
    }

    public String getProbeState() {
        return probeState;
    }

    public StepEntity getProbeLastProbe() {
        return probeLastProbe;
    }

    public String getProbeResult() {
        return probeResult;
    }

    public String getProbeStatus() {
        return probeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProbeInstance that = (StepProbeInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(probeDefinition, that.probeDefinition) && Objects.equals(probeState, that.probeState) && Objects.equals(probeLastProbe, that.probeLastProbe) && Objects.equals(probeResult, that.probeResult) && Objects.equals(probeStatus, that.probeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, probeDefinition, probeState, probeLastProbe, probeResult, probeStatus);
    }

    @Override
    public String toString() {
        return "StepProbeInstance{" + "id=" + id + "name=" + name + "probeDefinition=" + probeDefinition + "probeState=" + probeState + "probeLastProbe=" + probeLastProbe + "probeResult=" + probeResult + "probeStatus=" + probeStatus + "}";
    }
}