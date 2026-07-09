package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COST_ESTIMATION.
 * A cost estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (material, labor, tooling)
 * @param estimatedCost estimated cost value
 * @param costCurrency cost currency specification
 * @param costBreakdown cost breakdown items
 * @param estimationMethod estimation method used
 * @param estimationDate estimation date
 */
/**
 * Resolved COST_ESTIMATION.
 * A cost estimation entity.
 *
 * @param id STEP instance id
 * @param name estimation name
 * @param estimationType estimation type (material, labor, tooling)
 * @param estimatedCost estimated cost value
 * @param costCurrency cost currency specification
 * @param costBreakdown cost breakdown items
 * @param estimationMethod estimation method used
 * @param estimationDate estimation date
 */
public final class StepCostEstimation implements StepEntity {
    private final int id;
    private final String name;
    private final String estimationType;
    private final double estimatedCost;
    private final String costCurrency;
    private final List<StepEntity> costBreakdown;
    private final String estimationMethod;
    private final StepEntity estimationDate;

    public StepCostEstimation(int id, String name, String estimationType, double estimatedCost, String costCurrency, List<StepEntity> costBreakdown, String estimationMethod, StepEntity estimationDate) {
        this.id = id;
        this.name = name;
        this.estimationType = estimationType;
        this.estimatedCost = estimatedCost;
        this.costCurrency = costCurrency;
        this.costBreakdown = costBreakdown == null ? null : java.util.List.copyOf(costBreakdown);
        this.estimationMethod = estimationMethod;
        this.estimationDate = estimationDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEstimationType() {
        return estimationType;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public String getCostCurrency() {
        return costCurrency;
    }

    public List<StepEntity> getCostBreakdown() {
        return costBreakdown;
    }

    public String getEstimationMethod() {
        return estimationMethod;
    }

    public StepEntity getEstimationDate() {
        return estimationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCostEstimation that = (StepCostEstimation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(estimationType, that.estimationType) && estimatedCost == that.estimatedCost && Objects.equals(costCurrency, that.costCurrency) && Objects.equals(costBreakdown, that.costBreakdown) && Objects.equals(estimationMethod, that.estimationMethod) && Objects.equals(estimationDate, that.estimationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, estimationType, estimatedCost, costCurrency, costBreakdown, estimationMethod, estimationDate);
    }

    @Override
    public String toString() {
        return "StepCostEstimation{" + "id=" + id + "name=" + name + "estimationType=" + estimationType + "estimatedCost=" + estimatedCost + "costCurrency=" + costCurrency + "costBreakdown=" + costBreakdown + "estimationMethod=" + estimationMethod + "estimationDate=" + estimationDate + "}";
    }
}