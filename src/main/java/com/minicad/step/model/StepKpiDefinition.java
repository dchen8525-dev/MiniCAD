package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved KPI_DEFINITION.
 * A KPI definition entity.
 *
 * @param id STEP instance id
 * @param name KPI name
 * @param kpiId KPI identifier
 * @varianceDescription KPI variance description
 * @varianceFormula KPI variance calculation formula
 * @varianceTarget target variance value
 * @varianceThreshold threshold variance values
 * @varianceUnit KPI variance unit
 * @varianceStatus KPI variance status
 */
public final class StepKpiDefinition extends AbstractStepEntity {
    private final String kpiId;
    private final String varianceDescription;
    private final String varianceFormula;
    private final double varianceTarget;
    private final List<Double> varianceThreshold;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepKpiDefinition(int id, String name, String kpiId, String varianceDescription, String varianceFormula, double varianceTarget, List<Double> varianceThreshold, StepEntity varianceUnit, String varianceStatus) {
        super(id, name);
        this.kpiId = kpiId;
        this.varianceDescription = varianceDescription;
        this.varianceFormula = varianceFormula;
        this.varianceTarget = varianceTarget;
        this.varianceThreshold = varianceThreshold == null ? null : java.util.List.copyOf(varianceThreshold);
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public String getKpiId() {
        return kpiId;
    }

    public String getVarianceDescription() {
        return varianceDescription;
    }

    public String getVarianceFormula() {
        return varianceFormula;
    }

    public double getVarianceTarget() {
        return varianceTarget;
    }

    public List<Double> getVarianceThreshold() {
        return varianceThreshold;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("kpiId", kpiId);
        state.put("varianceDescription", varianceDescription);
        state.put("varianceFormula", varianceFormula);
        state.put("varianceTarget", varianceTarget);
        state.put("varianceThreshold", varianceThreshold);
        state.put("varianceUnit", varianceUnit);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
