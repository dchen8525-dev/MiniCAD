package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ANOMALY_RECORD.
 * An anomaly record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType anomaly variance type
 * @varianceDetection detection variance method
 * @varianceDate anomaly variance date
 * @varianceInvestigation investigation variance result
 * @varianceAction action variance taken
 * @varianceStatus record variance status
 */
/**
 * Resolved ANOMALY_RECORD.
 * An anomaly record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType anomaly variance type
 * @varianceDetection detection variance method
 * @varianceDate anomaly variance date
 * @varianceInvestigation investigation variance result
 * @varianceAction action variance taken
 * @varianceStatus record variance status
 */
public final class StepAnomalyRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceSystem;
    private final String varianceType;
    private final String varianceDetection;
    private final StepEntity varianceDate;
    private final String varianceInvestigation;
    private final String varianceAction;
    private final String varianceStatus;

    public StepAnomalyRecord(int id, String name, StepEntity varianceSystem, String varianceType, String varianceDetection, StepEntity varianceDate, String varianceInvestigation, String varianceAction, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceSystem = varianceSystem;
        this.varianceType = varianceType;
        this.varianceDetection = varianceDetection;
        this.varianceDate = varianceDate;
        this.varianceInvestigation = varianceInvestigation;
        this.varianceAction = varianceAction;
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

    public String getVarianceDetection() {
        return varianceDetection;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceInvestigation() {
        return varianceInvestigation;
    }

    public String getVarianceAction() {
        return varianceAction;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnomalyRecord that = (StepAnomalyRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceSystem, that.varianceSystem) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceDetection, that.varianceDetection) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceInvestigation, that.varianceInvestigation) && Objects.equals(varianceAction, that.varianceAction) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceSystem, varianceType, varianceDetection, varianceDate, varianceInvestigation, varianceAction, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepAnomalyRecord{" + "id=" + id + "name=" + name + "varianceSystem=" + varianceSystem + "varianceType=" + varianceType + "varianceDetection=" + varianceDetection + "varianceDate=" + varianceDate + "varianceInvestigation=" + varianceInvestigation + "varianceAction=" + varianceAction + "varianceStatus=" + varianceStatus + "}";
    }
}