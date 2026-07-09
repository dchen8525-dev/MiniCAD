package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COST_INSTANCE.
 * A cost instance entity.
 *
 * @param id STEP instance id
 * @param name cost instance name
 * @param costDefinition cost variance definition reference
 * @param costPlanned cost variance planned amount
 * @param costActual cost variance actual amount
 * @param costVariance cost variance difference
 * @param costBreakdown cost variance breakdown details
 * @param costStatus cost variance status
 */
/**
 * Resolved COST_INSTANCE.
 * A cost instance entity.
 *
 * @param id STEP instance id
 * @param name cost instance name
 * @param costDefinition cost variance definition reference
 * @param costPlanned cost variance planned amount
 * @param costActual cost variance actual amount
 * @param costVariance cost variance difference
 * @param costBreakdown cost variance breakdown details
 * @param costStatus cost variance status
 */
public final class StepCostInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity costDefinition;
    private final double costPlanned;
    private final double costActual;
    private final double costVariance;
    private final List<String> costBreakdown;
    private final String costStatus;

    public StepCostInstance(int id, String name, StepEntity costDefinition, double costPlanned, double costActual, double costVariance, List<String> costBreakdown, String costStatus) {
        this.id = id;
        this.name = name;
        this.costDefinition = costDefinition;
        this.costPlanned = costPlanned;
        this.costActual = costActual;
        this.costVariance = costVariance;
        this.costBreakdown = costBreakdown == null ? null : java.util.List.copyOf(costBreakdown);
        this.costStatus = costStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCostDefinition() {
        return costDefinition;
    }

    public double getCostPlanned() {
        return costPlanned;
    }

    public double getCostActual() {
        return costActual;
    }

    public double getCostVariance() {
        return costVariance;
    }

    public List<String> getCostBreakdown() {
        return costBreakdown;
    }

    public String getCostStatus() {
        return costStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCostInstance that = (StepCostInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(costDefinition, that.costDefinition) && costPlanned == that.costPlanned && costActual == that.costActual && costVariance == that.costVariance && Objects.equals(costBreakdown, that.costBreakdown) && Objects.equals(costStatus, that.costStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, costDefinition, costPlanned, costActual, costVariance, costBreakdown, costStatus);
    }

    @Override
    public String toString() {
        return "StepCostInstance{" + "id=" + id + "name=" + name + "costDefinition=" + costDefinition + "costPlanned=" + costPlanned + "costActual=" + costActual + "costVariance=" + costVariance + "costBreakdown=" + costBreakdown + "costStatus=" + costStatus + "}";
    }
}