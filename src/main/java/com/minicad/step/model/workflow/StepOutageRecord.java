package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved OUTAGE_RECORD.
 * An outage record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceCause outage variance cause
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceDuration outage variance duration
 * @varianceImpact impact variance description
 * @varianceStatus record variance status
 */
/**
 * Resolved OUTAGE_RECORD.
 * An outage record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceCause outage variance cause
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceDuration outage variance duration
 * @varianceImpact impact variance description
 * @varianceStatus record variance status
 */
public final class StepOutageRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final String varianceCause;
    private final StepEntity varianceStartTime;
    private final StepEntity varianceEndTime;
    private final double varianceDuration;
    private final String varianceImpact;
    private final String varianceStatus;

    public StepOutageRecord(int id, String name, StepEntity varianceSystem, String varianceCause, StepEntity varianceStartTime, StepEntity varianceEndTime, double varianceDuration, String varianceImpact, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceCause = varianceCause;
        this.varianceStartTime = varianceStartTime;
        this.varianceEndTime = varianceEndTime;
        this.varianceDuration = varianceDuration;
        this.varianceImpact = varianceImpact;
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

    public String getVarianceCause() {
        return varianceCause;
    }

    public StepEntity getVarianceStartTime() {
        return varianceStartTime;
    }

    public StepEntity getVarianceEndTime() {
        return varianceEndTime;
    }

    public double getVarianceDuration() {
        return varianceDuration;
    }

    public String getVarianceImpact() {
        return varianceImpact;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOutageRecord that = (StepOutageRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceCause, that.varianceCause) && Objects.equals(varianceStartTime, that.varianceStartTime) && Objects.equals(varianceEndTime, that.varianceEndTime) && varianceDuration == that.varianceDuration && Objects.equals(varianceImpact, that.varianceImpact) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceCause, varianceStartTime, varianceEndTime, varianceDuration, varianceImpact, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepOutageRecord{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceCause=" + varianceCause + "varianceStartTime=" + varianceStartTime + "varianceEndTime=" + varianceEndTime + "varianceDuration=" + varianceDuration + "varianceImpact=" + varianceImpact + "varianceStatus=" + varianceStatus + "}";
    }
}