package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EVENT_DEFINITION.
 * An event definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceEvent defined variance event
 * @varianceType event variance type (internal, external, time)
 * @varianceTrigger event variance trigger condition
 * @varianceResponse event variance response action
 * @variancePriority event variance priority
 * @varianceStatus definition variance status
 */
public final class StepEventDefinition extends AbstractStepEntity {
    private final String varianceEvent;
    private final String varianceType;
    private final String varianceTrigger;
    private final StepEntity varianceResponse;
    private final int variancePriority;
    private final String varianceStatus;

    public StepEventDefinition(int id, String name, String varianceEvent, String varianceType, String varianceTrigger, StepEntity varianceResponse, int variancePriority, String varianceStatus) {
        super(id, name);
        this.varianceEvent = varianceEvent;
        this.varianceType = varianceType;
        this.varianceTrigger = varianceTrigger;
        this.varianceResponse = varianceResponse;
        this.variancePriority = variancePriority;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceEvent() {
        return varianceEvent;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceTrigger() {
        return varianceTrigger;
    }

    public StepEntity getVarianceResponse() {
        return varianceResponse;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEvent", varianceEvent);
        state.put("varianceType", varianceType);
        state.put("varianceTrigger", varianceTrigger);
        state.put("varianceResponse", varianceResponse);
        state.put("variancePriority", variancePriority);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
