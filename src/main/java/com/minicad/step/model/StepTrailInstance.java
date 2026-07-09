package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTrailInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity trailDefinition;
    private final int trailEntries;
    private final StepEntity trailStartTime;
    private final StepEntity trailEndTime;
    private final String trailStatus;

    public StepTrailInstance(int id, String name, StepEntity trailDefinition, int trailEntries, StepEntity trailStartTime, StepEntity trailEndTime, String trailStatus) {
        this.id = id;
        this.name = name;
        this.trailDefinition = trailDefinition;
        this.trailEntries = trailEntries;
        this.trailStartTime = trailStartTime;
        this.trailEndTime = trailEndTime;
        this.trailStatus = trailStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrailInstance that = (StepTrailInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(trailDefinition, that.trailDefinition) && trailEntries == that.trailEntries && Objects.equals(trailStartTime, that.trailStartTime) && Objects.equals(trailEndTime, that.trailEndTime) && Objects.equals(trailStatus, that.trailStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, trailDefinition, trailEntries, trailStartTime, trailEndTime, trailStatus);
    }

    @Override
    public String toString() {
        return "StepTrailInstance{" + "id=" + id + "name=" + name + "trailDefinition=" + trailDefinition + "trailEntries=" + trailEntries + "trailStartTime=" + trailStartTime + "trailEndTime=" + trailEndTime + "trailStatus=" + trailStatus + "}";
    }
}