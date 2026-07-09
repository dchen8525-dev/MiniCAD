package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepActivityRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String activityType;
    private final String activityAction;
    private final StepEntity activityActor;
    private final StepEntity activityTarget;
    private final StepEntity activityTimestamp;
    private final List<String> activityDetails;
    private final String activityStatus;

    public StepActivityRecord(int id, String name, String activityType, String activityAction, StepEntity activityActor, StepEntity activityTarget, StepEntity activityTimestamp, List<String> activityDetails, String activityStatus) {
        this.id = id;
        this.name = name;
        this.activityType = activityType;
        this.activityAction = activityAction;
        this.activityActor = activityActor;
        this.activityTarget = activityTarget;
        this.activityTimestamp = activityTimestamp;
        this.activityDetails = activityDetails == null ? null : java.util.List.copyOf(activityDetails);
        this.activityStatus = activityStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActivityRecord that = (StepActivityRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(activityType, that.activityType) && Objects.equals(activityAction, that.activityAction) && Objects.equals(activityActor, that.activityActor) && Objects.equals(activityTarget, that.activityTarget) && Objects.equals(activityTimestamp, that.activityTimestamp) && Objects.equals(activityDetails, that.activityDetails) && Objects.equals(activityStatus, that.activityStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, activityType, activityAction, activityActor, activityTarget, activityTimestamp, activityDetails, activityStatus);
    }

    @Override
    public String toString() {
        return "StepActivityRecord{" + "id=" + id + "name=" + name + "activityType=" + activityType + "activityAction=" + activityAction + "activityActor=" + activityActor + "activityTarget=" + activityTarget + "activityTimestamp=" + activityTimestamp + "activityDetails=" + activityDetails + "activityStatus=" + activityStatus + "}";
    }
}