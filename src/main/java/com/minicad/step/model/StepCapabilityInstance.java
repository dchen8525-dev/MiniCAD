package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CAPABILITY_INSTANCE.
 * A capability instance entity.
 *
 * @param id STEP instance id
 * @param name capability instance name
 * @param capabilityDefinition capability variance definition reference
 * @param capabilityState capability variance state
 * @param capabilityScore capability variance score
 * @param capabilityHistory capability variance history records
 * @param capabilityStatus capability variance status
 */
public final class StepCapabilityInstance extends AbstractStepEntity {
    private final StepEntity capabilityDefinition;
    private final String capabilityState;
    private final double capabilityScore;
    private final List<String> capabilityHistory;
    private final String capabilityStatus;

    public StepCapabilityInstance(int id, String name, StepEntity capabilityDefinition, String capabilityState, double capabilityScore, List<String> capabilityHistory, String capabilityStatus) {
        super(id, name);
        this.capabilityDefinition = capabilityDefinition;
        this.capabilityState = capabilityState;
        this.capabilityScore = capabilityScore;
        this.capabilityHistory = capabilityHistory == null ? null : java.util.List.copyOf(capabilityHistory);
        this.capabilityStatus = capabilityStatus;
    }

    public StepEntity getCapabilityDefinition() {
        return capabilityDefinition;
    }

    public String getCapabilityState() {
        return capabilityState;
    }

    public double getCapabilityScore() {
        return capabilityScore;
    }

    public List<String> getCapabilityHistory() {
        return capabilityHistory;
    }

    public String getCapabilityStatus() {
        return capabilityStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("capabilityDefinition", capabilityDefinition);
        state.put("capabilityState", capabilityState);
        state.put("capabilityScore", capabilityScore);
        state.put("capabilityHistory", capabilityHistory);
        state.put("capabilityStatus", capabilityStatus);
        return state;
    }
}
