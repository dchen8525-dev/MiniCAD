package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRAIL_DEFINITION.
 * A trail definition entity.
 *
 * @param id STEP instance id
 * @param name trail name
 * @param trailType trail variance type
 * @param trailDescription trail variance description
 * @param trailEvents trail variance tracked events
 * @param trailRetention trail variance retention period
 * @param trailStatus trail variance status
 */
public final class StepTrailDefinition extends AbstractStepEntity {
    private final String trailType;
    private final String trailDescription;
    private final List<String> trailEvents;
    private final int trailRetention;
    private final String trailStatus;

    public StepTrailDefinition(int id, String name, String trailType, String trailDescription, List<String> trailEvents, int trailRetention, String trailStatus) {
        super(id, name);
        this.trailType = trailType;
        this.trailDescription = trailDescription;
        this.trailEvents = trailEvents == null ? null : java.util.List.copyOf(trailEvents);
        this.trailRetention = trailRetention;
        this.trailStatus = trailStatus;
    }

    public String getTrailType() {
        return trailType;
    }

    public String getTrailDescription() {
        return trailDescription;
    }

    public List<String> getTrailEvents() {
        return trailEvents;
    }

    public int getTrailRetention() {
        return trailRetention;
    }

    public String getTrailStatus() {
        return trailStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("trailType", trailType);
        state.put("trailDescription", trailDescription);
        state.put("trailEvents", trailEvents);
        state.put("trailRetention", trailRetention);
        state.put("trailStatus", trailStatus);
        return state;
    }
}
