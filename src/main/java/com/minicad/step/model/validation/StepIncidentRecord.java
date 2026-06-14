package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepIncidentRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final String varianceType;
    private final int varianceSeverity;
    private final StepEntity varianceStartTime;
    private final StepEntity varianceEndTime;
    private final String varianceResolution;
    private final String varianceStatus;

    public StepIncidentRecord(int id, String name, StepEntity varianceSystem, String varianceType, int varianceSeverity, StepEntity varianceStartTime, StepEntity varianceEndTime, String varianceResolution, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceType = varianceType;
        this.varianceSeverity = varianceSeverity;
        this.varianceStartTime = varianceStartTime;
        this.varianceEndTime = varianceEndTime;
        this.varianceResolution = varianceResolution;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIncidentRecord that = (StepIncidentRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceType, that.varianceType) && varianceSeverity == that.varianceSeverity && Objects.equals(varianceStartTime, that.varianceStartTime) && Objects.equals(varianceEndTime, that.varianceEndTime) && Objects.equals(varianceResolution, that.varianceResolution) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceType, varianceSeverity, varianceStartTime, varianceEndTime, varianceResolution, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepIncidentRecord{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceType=" + varianceType + "varianceSeverity=" + varianceSeverity + "varianceStartTime=" + varianceStartTime + "varianceEndTime=" + varianceEndTime + "varianceResolution=" + varianceResolution + "varianceStatus=" + varianceStatus + "}";
    }
}