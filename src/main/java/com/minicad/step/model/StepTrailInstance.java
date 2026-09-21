package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRAIL_INSTANCE.
 * A trail instance entity.
 *
 * @param id STEP instance id
 * @param name trail instance name
 * @param trailDefinition trail variance definition reference
 * @param trailEntries trail variance entry count
 * @param trailStartTime trail variance start time
 * @param trailEndTime trail variance end time
 * @param trailStatus trail variance status
 */
public final class StepTrailInstance extends AbstractStepEntity {
    private final StepEntity trailDefinition;
    private final int trailEntries;
    private final StepEntity trailStartTime;
    private final StepEntity trailEndTime;
    private final String trailStatus;

    public StepTrailInstance(int id, String name, StepEntity trailDefinition, int trailEntries, StepEntity trailStartTime, StepEntity trailEndTime, String trailStatus) {
        super(id, name);
        this.trailDefinition = trailDefinition;
        this.trailEntries = trailEntries;
        this.trailStartTime = trailStartTime;
        this.trailEndTime = trailEndTime;
        this.trailStatus = trailStatus;
    }

    public StepEntity getTrailDefinition() {
        return trailDefinition;
    }

    public int getTrailEntries() {
        return trailEntries;
    }

    public StepEntity getTrailStartTime() {
        return trailStartTime;
    }

    public StepEntity getTrailEndTime() {
        return trailEndTime;
    }

    public String getTrailStatus() {
        return trailStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("trailDefinition", trailDefinition);
        state.put("trailEntries", trailEntries);
        state.put("trailStartTime", trailStartTime);
        state.put("trailEndTime", trailEndTime);
        state.put("trailStatus", trailStatus);
        return state;
    }
}
