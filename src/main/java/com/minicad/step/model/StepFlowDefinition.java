package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FLOW_DEFINITION.
 * A flow definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFlow defined variance flow
 * @varianceFrom source variance element
 * @varianceTo target variance element
 * @varianceType flow variance type (material, information, energy)
 * @varianceRate flow variance rate
 * @varianceUnit flow variance unit
 * @varianceStatus definition variance status
 */
/**
 * Resolved FLOW_DEFINITION.
 * A flow definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFlow defined variance flow
 * @varianceFrom source variance element
 * @varianceTo target variance element
 * @varianceType flow variance type (material, information, energy)
 * @varianceRate flow variance rate
 * @varianceUnit flow variance unit
 * @varianceStatus definition variance status
 */
public final class StepFlowDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceFlow;
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceType;
    private final double varianceRate;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepFlowDefinition(int id, String name, StepEntity varianceFlow, StepEntity varianceFrom, StepEntity varianceTo, String varianceType, double varianceRate, StepEntity varianceUnit, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceFlow = varianceFlow;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceType = varianceType;
        this.varianceRate = varianceRate;
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceFlow() {
        return varianceFlow;
    }

    public StepEntity getVarianceFrom() {
        return varianceFrom;
    }

    public StepEntity getVarianceTo() {
        return varianceTo;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public double getVarianceRate() {
        return varianceRate;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFlowDefinition that = (StepFlowDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceFlow, that.varianceFlow) && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceType, that.varianceType) && varianceRate == that.varianceRate && Objects.equals(varianceUnit, that.varianceUnit) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceFlow, varianceFrom, varianceTo, varianceType, varianceRate, varianceUnit, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepFlowDefinition{" + "id=" + id + "name=" + name + "varianceFlow=" + varianceFlow + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceType=" + varianceType + "varianceRate=" + varianceRate + "varianceUnit=" + varianceUnit + "varianceStatus=" + varianceStatus + "}";
    }
}