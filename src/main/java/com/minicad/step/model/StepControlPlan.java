package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONTROL_PLAN.
 * A control plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceControlItems control variance items
 * @varianceParameters control variance parameters
 * @varianceLimits control variance limits (tolerances)
 * @varianceMethods control variance methods
 * @varianceResponse response variance actions for out-of-control
 * @varianceStatus plan variance status
 */
/**
 * Resolved CONTROL_PLAN.
 * A control plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @varianceControlItems control variance items
 * @varianceParameters control variance parameters
 * @varianceLimits control variance limits (tolerances)
 * @varianceMethods control variance methods
 * @varianceResponse response variance actions for out-of-control
 * @varianceStatus plan variance status
 */
public final class StepControlPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> varianceControlItems;
    private final List<Double> varianceParameters;
    private final List<Double> varianceLimits;
    private final List<String> varianceMethods;
    private final List<StepEntity> varianceResponse;
    private final String varianceStatus;

    public StepControlPlan(int id, String name, List<StepEntity> varianceControlItems, List<Double> varianceParameters, List<Double> varianceLimits, List<String> varianceMethods, List<StepEntity> varianceResponse, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceControlItems = varianceControlItems == null ? null : java.util.List.copyOf(varianceControlItems);
        this.varianceParameters = varianceParameters == null ? null : java.util.List.copyOf(varianceParameters);
        this.varianceLimits = varianceLimits == null ? null : java.util.List.copyOf(varianceLimits);
        this.varianceMethods = varianceMethods == null ? null : java.util.List.copyOf(varianceMethods);
        this.varianceResponse = varianceResponse == null ? null : java.util.List.copyOf(varianceResponse);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getVarianceControlItems() {
        return varianceControlItems;
    }

    public List<Double> getVarianceParameters() {
        return varianceParameters;
    }

    public List<Double> getVarianceLimits() {
        return varianceLimits;
    }

    public List<String> getVarianceMethods() {
        return varianceMethods;
    }

    public List<StepEntity> getVarianceResponse() {
        return varianceResponse;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepControlPlan that = (StepControlPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceControlItems, that.varianceControlItems) && Objects.equals(varianceParameters, that.varianceParameters) && Objects.equals(varianceLimits, that.varianceLimits) && Objects.equals(varianceMethods, that.varianceMethods) && Objects.equals(varianceResponse, that.varianceResponse) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceControlItems, varianceParameters, varianceLimits, varianceMethods, varianceResponse, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepControlPlan{" + "id=" + id + "name=" + name + "varianceControlItems=" + varianceControlItems + "varianceParameters=" + varianceParameters + "varianceLimits=" + varianceLimits + "varianceMethods=" + varianceMethods + "varianceResponse=" + varianceResponse + "varianceStatus=" + varianceStatus + "}";
    }
}