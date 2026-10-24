package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepFlowDefinition extends AbstractStepEntity {
    private final StepEntity varianceFlow;
    private final StepEntity varianceFrom;
    private final StepEntity varianceTo;
    private final String varianceType;
    private final double varianceRate;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepFlowDefinition(int id, String name, StepEntity varianceFlow, StepEntity varianceFrom, StepEntity varianceTo, String varianceType, double varianceRate, StepEntity varianceUnit, String varianceStatus) {
        super(id, name);
        this.varianceFlow = varianceFlow;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceType = varianceType;
        this.varianceRate = varianceRate;
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceFlow", varianceFlow, "varianceFrom", varianceFrom,
                "varianceTo", varianceTo, "varianceType", varianceType, "varianceRate", varianceRate,
                "varianceUnit", varianceUnit, "varianceStatus", varianceStatus);
    }
}
