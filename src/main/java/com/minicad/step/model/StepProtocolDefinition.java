package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROTOCOL_DEFINITION.
 * A protocol definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceProtocol defined variance protocol
 * @varianceType protocol variance type (communication, data, control)
 * @varianceFormat protocol variance format specification
 * @varianceSequence protocol variance sequence/steps
 * @varianceStandard protocol variance standard reference
 * @varianceStatus definition variance status
 */
public final class StepProtocolDefinition extends AbstractStepEntity {
    private final String varianceProtocol;
    private final String varianceType;
    private final String varianceFormat;
    private final List<String> varianceSequence;
    private final String varianceStandard;
    private final String varianceStatus;

    public StepProtocolDefinition(int id, String name, String varianceProtocol, String varianceType, String varianceFormat, List<String> varianceSequence, String varianceStandard, String varianceStatus) {
        super(id, name);
        this.varianceProtocol = varianceProtocol;
        this.varianceType = varianceType;
        this.varianceFormat = varianceFormat;
        this.varianceSequence = varianceSequence == null ? null : java.util.List.copyOf(varianceSequence);
        this.varianceStandard = varianceStandard;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceProtocol() {
        return varianceProtocol;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public List<String> getVarianceSequence() {
        return varianceSequence;
    }

    public String getVarianceStandard() {
        return varianceStandard;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceProtocol", varianceProtocol);
        state.put("varianceType", varianceType);
        state.put("varianceFormat", varianceFormat);
        state.put("varianceSequence", varianceSequence);
        state.put("varianceStandard", varianceStandard);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
