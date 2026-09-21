package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepProbeInstance extends AbstractStepEntity {
    private final StepEntity probeDefinition;
    private final String probeState;
    private final StepEntity probeLastProbe;
    private final String probeResult;
    private final String probeStatus;

    public StepProbeInstance(int id, String name, StepEntity probeDefinition, String probeState, StepEntity probeLastProbe, String probeResult, String probeStatus) {
        super(id, name);
        this.probeDefinition = probeDefinition;
        this.probeState = probeState;
        this.probeLastProbe = probeLastProbe;
        this.probeResult = probeResult;
        this.probeStatus = probeStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("probeDefinition", probeDefinition);
        state.put("probeState", probeState);
        state.put("probeLastProbe", probeLastProbe);
        state.put("probeResult", probeResult);
        state.put("probeStatus", probeStatus);
        return state;
    }
}
