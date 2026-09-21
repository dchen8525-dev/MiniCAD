package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved INCIDENT_RECORD.
 * An incident record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType incident variance type
 * @varianceSeverity severity variance level
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceResolution resolution variance action
 * @varianceStatus record variance status
 */
public final class StepIncidentRecord extends AbstractStepEntity {
    private final StepEntity varianceSystem;
    private final String varianceType;
    private final int varianceSeverity;
    private final StepEntity varianceStartTime;
    private final StepEntity varianceEndTime;
    private final String varianceResolution;
    private final String varianceStatus;

    public StepIncidentRecord(int id, String name, StepEntity varianceSystem, String varianceType, int varianceSeverity, StepEntity varianceStartTime, StepEntity varianceEndTime, String varianceResolution, String varianceStatus) {
        super(id, name);
        this.varianceSystem = varianceSystem;
        this.varianceType = varianceType;
        this.varianceSeverity = varianceSeverity;
        this.varianceStartTime = varianceStartTime;
        this.varianceEndTime = varianceEndTime;
        this.varianceResolution = varianceResolution;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceSystem() {
        return varianceSystem;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public int getVarianceSeverity() {
        return varianceSeverity;
    }

    public StepEntity getVarianceStartTime() {
        return varianceStartTime;
    }

    public StepEntity getVarianceEndTime() {
        return varianceEndTime;
    }

    public String getVarianceResolution() {
        return varianceResolution;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceSystem", varianceSystem);
        state.put("varianceType", varianceType);
        state.put("varianceSeverity", varianceSeverity);
        state.put("varianceStartTime", varianceStartTime);
        state.put("varianceEndTime", varianceEndTime);
        state.put("varianceResolution", varianceResolution);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
