package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved FAILURE_MODE.
 * A failure mode entity.
 *
 * @param id STEP instance id
 * @param name mode name
 * @varianceItem item variance susceptible to failure
 * @varianceType failure variance type
 * @varianceCause failure variance causes
 * @varianceEffect failure variance effects
 * @varianceSeverity severity variance rating
 * @varianceDetection detection variance rating
 * @varianceRisk risk variance priority number
 */
public final class StepFailureMode extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceType;
    private final List<String> varianceCause;
    private final List<String> varianceEffect;
    private final int varianceSeverity;
    private final int varianceDetection;
    private final int varianceRisk;

    public StepFailureMode(int id, String name, StepEntity varianceItem, String varianceType, List<String> varianceCause, List<String> varianceEffect, int varianceSeverity, int varianceDetection, int varianceRisk) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceCause = varianceCause == null ? null : java.util.List.copyOf(varianceCause);
        this.varianceEffect = varianceEffect == null ? null : java.util.List.copyOf(varianceEffect);
        this.varianceSeverity = varianceSeverity;
        this.varianceDetection = varianceDetection;
        this.varianceRisk = varianceRisk;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public List<String> getVarianceCause() {
        return varianceCause;
    }

    public List<String> getVarianceEffect() {
        return varianceEffect;
    }

    public int getVarianceSeverity() {
        return varianceSeverity;
    }

    public int getVarianceDetection() {
        return varianceDetection;
    }

    public int getVarianceRisk() {
        return varianceRisk;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceItem", varianceItem, "varianceType", varianceType,
                "varianceCause", varianceCause, "varianceEffect", varianceEffect,
                "varianceSeverity", varianceSeverity, "varianceDetection", varianceDetection,
                "varianceRisk", varianceRisk);
    }
}
