package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ENVIRONMENTAL_IMPACT.
 * An environmental impact entity.
 *
 * @param id STEP instance id
 * @param name impact name
 * @param impactType impact type (energy, waste, emissions)
 * @param impactValue impact value measurement
 * @param impactUnit impact unit specification
 * @varianceTarget target variance reduction value
 * @param mitigationMeasures mitigation measures
 * @varianceStatus impact variance status
 */
/**
 * Resolved ENVIRONMENTAL_IMPACT.
 * An environmental impact entity.
 *
 * @param id STEP instance id
 * @param name impact name
 * @param impactType impact type (energy, waste, emissions)
 * @param impactValue impact value measurement
 * @param impactUnit impact unit specification
 * @varianceTarget target variance reduction value
 * @param mitigationMeasures mitigation measures
 * @varianceStatus impact variance status
 */
public final class StepEnvironmentalImpact implements StepEntity {
    private final int id;
    private final String name;
    private final String impactType;
    private final double impactValue;
    private final StepEntity impactUnit;
    private final double varianceTarget;
    private final List<StepEntity> mitigationMeasures;
    private final String varianceStatus;

    public StepEnvironmentalImpact(int id, String name, String impactType, double impactValue, StepEntity impactUnit, double varianceTarget, List<StepEntity> mitigationMeasures, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.impactType = impactType;
        this.impactValue = impactValue;
        this.impactUnit = impactUnit;
        this.varianceTarget = varianceTarget;
        this.mitigationMeasures = mitigationMeasures == null ? null : java.util.List.copyOf(mitigationMeasures);
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImpactType() {
        return impactType;
    }

    public double getImpactValue() {
        return impactValue;
    }

    public StepEntity getImpactUnit() {
        return impactUnit;
    }

    public double getVarianceTarget() {
        return varianceTarget;
    }

    public List<StepEntity> getMitigationMeasures() {
        return mitigationMeasures;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEnvironmentalImpact that = (StepEnvironmentalImpact) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(impactType, that.impactType) && impactValue == that.impactValue && Objects.equals(impactUnit, that.impactUnit) && varianceTarget == that.varianceTarget && Objects.equals(mitigationMeasures, that.mitigationMeasures) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, impactType, impactValue, impactUnit, varianceTarget, mitigationMeasures, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepEnvironmentalImpact{" + "id=" + id + "name=" + name + "impactType=" + impactType + "impactValue=" + impactValue + "impactUnit=" + impactUnit + "varianceTarget=" + varianceTarget + "mitigationMeasures=" + mitigationMeasures + "varianceStatus=" + varianceStatus + "}";
    }
}