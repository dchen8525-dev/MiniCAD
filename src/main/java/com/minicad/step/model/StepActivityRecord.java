package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACTIVITY_RECORD.
 * An activity record entity.
 *
 * @param id STEP instance id
 * @param name activity name
 * @param activityType activity variance type
 * @param activityAction activity variance action description
 * @param activityActor activity variance actor reference
 * @param activityTarget activity variance target reference
 * @param activityTimestamp activity variance timestamp
 * @param activityDetails activity variance details
 * @param activityStatus activity variance status
 */
public final class StepActivityRecord extends AbstractStepEntity {
    private final String activityType;
    private final String activityAction;
    private final StepEntity activityActor;
    private final StepEntity activityTarget;
    private final StepEntity activityTimestamp;
    private final List<String> activityDetails;
    private final String activityStatus;

    public StepActivityRecord(int id, String name, String activityType, String activityAction, StepEntity activityActor, StepEntity activityTarget, StepEntity activityTimestamp, List<String> activityDetails, String activityStatus) {
        super(id, name);
        this.activityType = activityType;
        this.activityAction = activityAction;
        this.activityActor = activityActor;
        this.activityTarget = activityTarget;
        this.activityTimestamp = activityTimestamp;
        this.activityDetails = activityDetails == null ? null : java.util.List.copyOf(activityDetails);
        this.activityStatus = activityStatus;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getActivityAction() {
        return activityAction;
    }

    public StepEntity getActivityActor() {
        return activityActor;
    }

    public StepEntity getActivityTarget() {
        return activityTarget;
    }

    public StepEntity getActivityTimestamp() {
        return activityTimestamp;
    }

    public List<String> getActivityDetails() {
        return activityDetails;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("activityType", activityType);
        state.put("activityAction", activityAction);
        state.put("activityActor", activityActor);
        state.put("activityTarget", activityTarget);
        state.put("activityTimestamp", activityTimestamp);
        state.put("activityDetails", activityDetails);
        state.put("activityStatus", activityStatus);
        return state;
    }
}
