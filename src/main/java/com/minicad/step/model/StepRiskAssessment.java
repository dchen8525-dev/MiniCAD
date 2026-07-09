package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RISK_ASSESSMENT.
 * A risk assessment entity.
 *
 * @param id STEP instance id
 * @param name assessment name
 * @varianceItem assessed variance item
 * @varianceHazards identified variance hazards
 * @varianceRisks risk variance ratings
 * @varianceMitigations mitigation variance measures
 * @varianceResidual residual variance risk after mitigation
 * @varianceStatus assessment variance status
 */
/**
 * Resolved RISK_ASSESSMENT.
 * A risk assessment entity.
 *
 * @param id STEP instance id
 * @param name assessment name
 * @varianceItem assessed variance item
 * @varianceHazards identified variance hazards
 * @varianceRisks risk variance ratings
 * @varianceMitigations mitigation variance measures
 * @varianceResidual residual variance risk after mitigation
 * @varianceStatus assessment variance status
 */
public final class StepRiskAssessment implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<String> varianceHazards;
    private final List<Integer> varianceRisks;
    private final List<StepEntity> varianceMitigations;
    private final int varianceResidual;
    private final String varianceStatus;

    public StepRiskAssessment(int id, String name, StepEntity varianceItem, List<String> varianceHazards, List<Integer> varianceRisks, List<StepEntity> varianceMitigations, int varianceResidual, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceHazards = varianceHazards == null ? null : java.util.List.copyOf(varianceHazards);
        this.varianceRisks = varianceRisks == null ? null : java.util.List.copyOf(varianceRisks);
        this.varianceMitigations = varianceMitigations == null ? null : java.util.List.copyOf(varianceMitigations);
        this.varianceResidual = varianceResidual;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public List<String> getVarianceHazards() {
        return varianceHazards;
    }

    public List<Integer> getVarianceRisks() {
        return varianceRisks;
    }

    public List<StepEntity> getVarianceMitigations() {
        return varianceMitigations;
    }

    public int getVarianceResidual() {
        return varianceResidual;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRiskAssessment that = (StepRiskAssessment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceHazards, that.varianceHazards) && Objects.equals(varianceRisks, that.varianceRisks) && Objects.equals(varianceMitigations, that.varianceMitigations) && varianceResidual == that.varianceResidual && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceHazards, varianceRisks, varianceMitigations, varianceResidual, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepRiskAssessment{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceHazards=" + varianceHazards + "varianceRisks=" + varianceRisks + "varianceMitigations=" + varianceMitigations + "varianceResidual=" + varianceResidual + "varianceStatus=" + varianceStatus + "}";
    }
}