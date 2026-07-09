package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved QUALITY_PLAN.
 * A quality plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param planId plan identifier
 * @varianceItems quality variance control items
 * @varianceMethods inspection variance methods
 * @varianceCriteria acceptance variance criteria
 * @varianceFrequency inspection variance frequency
 * @varianceStatus plan variance status
 */
/**
 * Resolved QUALITY_PLAN.
 * A quality plan entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param planId plan identifier
 * @varianceItems quality variance control items
 * @varianceMethods inspection variance methods
 * @varianceCriteria acceptance variance criteria
 * @varianceFrequency inspection variance frequency
 * @varianceStatus plan variance status
 */
public final class StepQualityPlan implements StepEntity {
    private final int id;
    private final String name;
    private final String planId;
    private final List<StepEntity> varianceItems;
    private final List<String> varianceMethods;
    private final List<StepEntity> varianceCriteria;
    private final String varianceFrequency;
    private final String varianceStatus;

    public StepQualityPlan(int id, String name, String planId, List<StepEntity> varianceItems, List<String> varianceMethods, List<StepEntity> varianceCriteria, String varianceFrequency, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.planId = planId;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceMethods = varianceMethods == null ? null : java.util.List.copyOf(varianceMethods);
        this.varianceCriteria = varianceCriteria == null ? null : java.util.List.copyOf(varianceCriteria);
        this.varianceFrequency = varianceFrequency;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanId() {
        return planId;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<String> getVarianceMethods() {
        return varianceMethods;
    }

    public List<StepEntity> getVarianceCriteria() {
        return varianceCriteria;
    }

    public String getVarianceFrequency() {
        return varianceFrequency;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQualityPlan that = (StepQualityPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(planId, that.planId) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceMethods, that.varianceMethods) && Objects.equals(varianceCriteria, that.varianceCriteria) && Objects.equals(varianceFrequency, that.varianceFrequency) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planId, varianceItems, varianceMethods, varianceCriteria, varianceFrequency, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepQualityPlan{" + "id=" + id + "name=" + name + "planId=" + planId + "varianceItems=" + varianceItems + "varianceMethods=" + varianceMethods + "varianceCriteria=" + varianceCriteria + "varianceFrequency=" + varianceFrequency + "varianceStatus=" + varianceStatus + "}";
    }
}