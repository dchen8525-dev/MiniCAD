package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRiskAssessment extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final List<String> varianceHazards;
    private final List<Integer> varianceRisks;
    private final List<StepEntity> varianceMitigations;
    private final int varianceResidual;
    private final String varianceStatus;

    public StepRiskAssessment(int id, String name, StepEntity varianceItem, List<String> varianceHazards, List<Integer> varianceRisks, List<StepEntity> varianceMitigations, int varianceResidual, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceHazards = varianceHazards == null ? null : java.util.List.copyOf(varianceHazards);
        this.varianceRisks = varianceRisks == null ? null : java.util.List.copyOf(varianceRisks);
        this.varianceMitigations = varianceMitigations == null ? null : java.util.List.copyOf(varianceMitigations);
        this.varianceResidual = varianceResidual;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceHazards", varianceHazards);
        state.put("varianceRisks", varianceRisks);
        state.put("varianceMitigations", varianceMitigations);
        state.put("varianceResidual", varianceResidual);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
