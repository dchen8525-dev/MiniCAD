package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRACEABILITY_RECORD.
 * A traceability record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem traced variance item
 * @varianceOrigin origin variance source
 * @variancePath trace variance path/chain
 * @varianceDestination destination variance reference
 * @varianceDate trace variance date
 * @varianceStatus record variance status
 */
/**
 * Resolved TRACEABILITY_RECORD.
 * A traceability record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem traced variance item
 * @varianceOrigin origin variance source
 * @variancePath trace variance path/chain
 * @varianceDestination destination variance reference
 * @varianceDate trace variance date
 * @varianceStatus record variance status
 */
public final class StepTraceabilityRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final StepEntity varianceOrigin;
    private final List<StepEntity> variancePath;
    private final StepEntity varianceDestination;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepTraceabilityRecord(int id, String name, StepEntity varianceItem, StepEntity varianceOrigin, List<StepEntity> variancePath, StepEntity varianceDestination, StepEntity varianceDate, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceOrigin = varianceOrigin;
        this.variancePath = variancePath == null ? null : java.util.List.copyOf(variancePath);
        this.varianceDestination = varianceDestination;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public StepEntity getVarianceOrigin() {
        return varianceOrigin;
    }

    public List<StepEntity> getVariancePath() {
        return variancePath;
    }

    public StepEntity getVarianceDestination() {
        return varianceDestination;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTraceabilityRecord that = (StepTraceabilityRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceOrigin, that.varianceOrigin) && Objects.equals(variancePath, that.variancePath) && Objects.equals(varianceDestination, that.varianceDestination) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceOrigin, variancePath, varianceDestination, varianceDate, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTraceabilityRecord{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceOrigin=" + varianceOrigin + "variancePath=" + variancePath + "varianceDestination=" + varianceDestination + "varianceDate=" + varianceDate + "varianceStatus=" + varianceStatus + "}";
    }
}