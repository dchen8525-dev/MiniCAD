package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTrailDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String trailType;
    private final String trailDescription;
    private final List<String> trailEvents;
    private final int trailRetention;
    private final String trailStatus;

    public StepTrailDefinition(int id, String name, String trailType, String trailDescription, List<String> trailEvents, int trailRetention, String trailStatus) {
        this.id = id;
        this.name = name;
        this.trailType = trailType;
        this.trailDescription = trailDescription;
        this.trailEvents = trailEvents == null ? null : java.util.List.copyOf(trailEvents);
        this.trailRetention = trailRetention;
        this.trailStatus = trailStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTrailDefinition that = (StepTrailDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(trailType, that.trailType) && Objects.equals(trailDescription, that.trailDescription) && Objects.equals(trailEvents, that.trailEvents) && trailRetention == that.trailRetention && Objects.equals(trailStatus, that.trailStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, trailType, trailDescription, trailEvents, trailRetention, trailStatus);
    }

    @Override
    public String toString() {
        return "StepTrailDefinition{" + "id=" + id + "name=" + name + "trailType=" + trailType + "trailDescription=" + trailDescription + "trailEvents=" + trailEvents + "trailRetention=" + trailRetention + "trailStatus=" + trailStatus + "}";
    }
}