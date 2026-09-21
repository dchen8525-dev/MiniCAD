package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CAPABILITY_DEFINITION.
 * A capability definition entity.
 *
 * @param id STEP instance id
 * @param name capability name
 * @param capabilityType capability variance type
 * @param capabilityDescription capability variance description
 * @param capabilityParameters capability variance parameters
 * @param capabilityLevel capability variance level
 * @param capabilityStatus capability variance status
 */
public final class StepCapabilityDefinition extends AbstractStepEntity {
    private final String capabilityType;
    private final String capabilityDescription;
    private final List<String> capabilityParameters;
    private final int capabilityLevel;
    private final String capabilityStatus;

    public StepCapabilityDefinition(int id, String name, String capabilityType, String capabilityDescription, List<String> capabilityParameters, int capabilityLevel, String capabilityStatus) {
        super(id, name);
        this.capabilityType = capabilityType;
        this.capabilityDescription = capabilityDescription;
        this.capabilityParameters = capabilityParameters == null ? null : java.util.List.copyOf(capabilityParameters);
        this.capabilityLevel = capabilityLevel;
        this.capabilityStatus = capabilityStatus;
    }

    public String getCapabilityType() {
        return capabilityType;
    }

    public String getCapabilityDescription() {
        return capabilityDescription;
    }

    public List<String> getCapabilityParameters() {
        return capabilityParameters;
    }

    public int getCapabilityLevel() {
        return capabilityLevel;
    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("capabilityType", capabilityType);
        state.put("capabilityDescription", capabilityDescription);
        state.put("capabilityParameters", capabilityParameters);
        state.put("capabilityLevel", capabilityLevel);
        state.put("capabilityStatus", capabilityStatus);
        return state;
    }
}
