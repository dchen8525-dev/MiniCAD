package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved KPI_MEASUREMENT.
 * A KPI measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @varianceKpi KPI variance reference
 * @varianceValue measured variance value
 * @varianceDate measurement variance date
 * @variancePeriod measurement variance period
 * @varianceStatus measurement variance status
 * @varianceComment measurement variance comment
 */
/**
 * Resolved KPI_MEASUREMENT.
 * A KPI measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @varianceKpi KPI variance reference
 * @varianceValue measured variance value
 * @varianceDate measurement variance date
 * @variancePeriod measurement variance period
 * @varianceStatus measurement variance status
 * @varianceComment measurement variance comment
 */
public final class StepKpiMeasurement implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceKpi;
    private final double varianceValue;
    private final StepEntity varianceDate;
    private final String variancePeriod;
    private final String varianceStatus;
    private final String varianceComment;

    public StepKpiMeasurement(int id, String name, StepEntity varianceKpi, double varianceValue, StepEntity varianceDate, String variancePeriod, String varianceStatus, String varianceComment) {
        this.id = id;
        this.name = name;
        this.varianceKpi = varianceKpi;
        this.varianceValue = varianceValue;
        this.varianceDate = varianceDate;
        this.variancePeriod = variancePeriod;
        this.varianceStatus = varianceStatus;
        this.varianceComment = varianceComment;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceKpi() {
        return varianceKpi;
    }

    public double getVarianceValue() {
        return varianceValue;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    public String getVarianceComment() {
        return varianceComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKpiMeasurement that = (StepKpiMeasurement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceKpi, that.varianceKpi) && varianceValue == that.varianceValue && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(variancePeriod, that.variancePeriod) && Objects.equals(varianceStatus, that.varianceStatus) && Objects.equals(varianceComment, that.varianceComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceKpi, varianceValue, varianceDate, variancePeriod, varianceStatus, varianceComment);
    }

    @Override
    public String toString() {
        return "StepKpiMeasurement{" + "id=" + id + "name=" + name + "varianceKpi=" + varianceKpi + "varianceValue=" + varianceValue + "varianceDate=" + varianceDate + "variancePeriod=" + variancePeriod + "varianceStatus=" + varianceStatus + "varianceComment=" + varianceComment + "}";
    }
}