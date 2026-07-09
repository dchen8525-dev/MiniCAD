package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepEventDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceEvent;
    private final String varianceType;
    private final String varianceTrigger;
    private final StepEntity varianceResponse;
    private final int variancePriority;
    private final String varianceStatus;

    public StepEventDefinition(int id, String name, String varianceEvent, String varianceType, String varianceTrigger, StepEntity varianceResponse, int variancePriority, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEvent = varianceEvent;
        this.varianceType = varianceType;
        this.varianceTrigger = varianceTrigger;
        this.varianceResponse = varianceResponse;
        this.variancePriority = variancePriority;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEventDefinition that = (StepEventDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEvent, that.varianceEvent) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceTrigger, that.varianceTrigger) && Objects.equals(varianceResponse, that.varianceResponse) && variancePriority == that.variancePriority && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEvent, varianceType, varianceTrigger, varianceResponse, variancePriority, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepEventDefinition{" + "id=" + id + "name=" + name + "varianceEvent=" + varianceEvent + "varianceType=" + varianceType + "varianceTrigger=" + varianceTrigger + "varianceResponse=" + varianceResponse + "variancePriority=" + variancePriority + "varianceStatus=" + varianceStatus + "}";
    }
}