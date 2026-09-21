package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepProbeDefinition extends AbstractStepEntity {
    private final String probeType;
    private final StepEntity probeTarget;
    private final List<String> probeParameters;
    private final int probeInterval;
    private final String probeStatus;

    public StepProbeDefinition(int id, String name, String probeType, StepEntity probeTarget, List<String> probeParameters, int probeInterval, String probeStatus) {
        super(id, name);
        this.probeType = probeType;
        this.probeTarget = probeTarget;
        this.probeParameters = probeParameters == null ? null : java.util.List.copyOf(probeParameters);
        this.probeInterval = probeInterval;
        this.probeStatus = probeStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("probeType", probeType);
        state.put("probeTarget", probeTarget);
        state.put("probeParameters", probeParameters);
        state.put("probeInterval", probeInterval);
        state.put("probeStatus", probeStatus);
        return state;
    }
}
