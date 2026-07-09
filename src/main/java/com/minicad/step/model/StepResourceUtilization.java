package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESOURCE_UTILIZATION.
 * A resource utilization entity.
 *
 * @param id STEP instance id
 * @param name utilization name
 * @varianceResource resource variance reference
 * @varianceUtilization utilization variance percentage
 * @variancePeriod utilization variance period
 * @variancePeak peak variance utilization
 * @varianceAverage average variance utilization
 * @varianceStatus utilization variance status
 */
/**
 * Resolved RESOURCE_UTILIZATION.
 * A resource utilization entity.
 *
 * @param id STEP instance id
 * @param name utilization name
 * @varianceResource resource variance reference
 * @varianceUtilization utilization variance percentage
 * @variancePeriod utilization variance period
 * @variancePeak peak variance utilization
 * @varianceAverage average variance utilization
 * @varianceStatus utilization variance status
 */
public final class StepResourceUtilization implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceResource;
    private final double varianceUtilization;
    private final String variancePeriod;
    private final double variancePeak;
    private final double varianceAverage;
    private final String varianceStatus;

    public StepResourceUtilization(int id, String name, StepEntity varianceResource, double varianceUtilization, String variancePeriod, double variancePeak, double varianceAverage, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceResource = varianceResource;
        this.varianceUtilization = varianceUtilization;
        this.variancePeriod = variancePeriod;
        this.variancePeak = variancePeak;
        this.varianceAverage = varianceAverage;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceResource() {
        return varianceResource;
    }

    public double getVarianceUtilization() {
        return varianceUtilization;
    }

    public String getVariancePeriod() {
        return variancePeriod;
    }

    public double getVariancePeak() {
        return variancePeak;
    }

    public double getVarianceAverage() {
        return varianceAverage;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResourceUtilization that = (StepResourceUtilization) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceResource, that.varianceResource) && varianceUtilization == that.varianceUtilization && Objects.equals(variancePeriod, that.variancePeriod) && variancePeak == that.variancePeak && varianceAverage == that.varianceAverage && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceResource, varianceUtilization, variancePeriod, variancePeak, varianceAverage, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepResourceUtilization{" + "id=" + id + "name=" + name + "varianceResource=" + varianceResource + "varianceUtilization=" + varianceUtilization + "variancePeriod=" + variancePeriod + "variancePeak=" + variancePeak + "varianceAverage=" + varianceAverage + "varianceStatus=" + varianceStatus + "}";
    }
}