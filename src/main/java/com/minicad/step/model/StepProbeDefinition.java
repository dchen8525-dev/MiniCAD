package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepProbeDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String probeType;
    private final StepEntity probeTarget;
    private final List<String> probeParameters;
    private final int probeInterval;
    private final String probeStatus;

    public StepProbeDefinition(int id, String name, String probeType, StepEntity probeTarget, List<String> probeParameters, int probeInterval, String probeStatus) {
        this.id = id;
        this.name = name;
        this.probeType = probeType;
        this.probeTarget = probeTarget;
        this.probeParameters = probeParameters == null ? null : java.util.List.copyOf(probeParameters);
        this.probeInterval = probeInterval;
        this.probeStatus = probeStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProbeType() {
        return probeType;
    }

    public StepEntity getProbeTarget() {
        return probeTarget;
    }

    public List<String> getProbeParameters() {
        return probeParameters;
    }

    public int getProbeInterval() {
        return probeInterval;
    }

    public String getProbeStatus() {
        return probeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProbeDefinition that = (StepProbeDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(probeType, that.probeType) && Objects.equals(probeTarget, that.probeTarget) && Objects.equals(probeParameters, that.probeParameters) && probeInterval == that.probeInterval && Objects.equals(probeStatus, that.probeStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, probeType, probeTarget, probeParameters, probeInterval, probeStatus);
    }

    @Override
    public String toString() {
        return "StepProbeDefinition{" + "id=" + id + "name=" + name + "probeType=" + probeType + "probeTarget=" + probeTarget + "probeParameters=" + probeParameters + "probeInterval=" + probeInterval + "probeStatus=" + probeStatus + "}";
    }
}