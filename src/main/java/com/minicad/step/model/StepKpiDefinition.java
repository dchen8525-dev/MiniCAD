package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepKpiDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String kpiId;
    private final String varianceDescription;
    private final String varianceFormula;
    private final double varianceTarget;
    private final List<Double> varianceThreshold;
    private final StepEntity varianceUnit;
    private final String varianceStatus;

    public StepKpiDefinition(int id, String name, String kpiId, String varianceDescription, String varianceFormula, double varianceTarget, List<Double> varianceThreshold, StepEntity varianceUnit, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.kpiId = kpiId;
        this.varianceDescription = varianceDescription;
        this.varianceFormula = varianceFormula;
        this.varianceTarget = varianceTarget;
        this.varianceThreshold = varianceThreshold == null ? null : java.util.List.copyOf(varianceThreshold);
        this.varianceUnit = varianceUnit;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepKpiDefinition that = (StepKpiDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(kpiId, that.kpiId) && Objects.equals(varianceDescription, that.varianceDescription) && Objects.equals(varianceFormula, that.varianceFormula) && varianceTarget == that.varianceTarget && Objects.equals(varianceThreshold, that.varianceThreshold) && Objects.equals(varianceUnit, that.varianceUnit) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, kpiId, varianceDescription, varianceFormula, varianceTarget, varianceThreshold, varianceUnit, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepKpiDefinition{" + "id=" + id + "name=" + name + "kpiId=" + kpiId + "varianceDescription=" + varianceDescription + "varianceFormula=" + varianceFormula + "varianceTarget=" + varianceTarget + "varianceThreshold=" + varianceThreshold + "varianceUnit=" + varianceUnit + "varianceStatus=" + varianceStatus + "}";
    }
}