package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepFailureMode implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceType;
    private final List<String> varianceCause;
    private final List<String> varianceEffect;
    private final int varianceSeverity;
    private final int varianceDetection;
    private final int varianceRisk;

    public StepFailureMode(int id, String name, StepEntity varianceItem, String varianceType, List<String> varianceCause, List<String> varianceEffect, int varianceSeverity, int varianceDetection, int varianceRisk) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceCause = varianceCause == null ? null : java.util.List.copyOf(varianceCause);
        this.varianceEffect = varianceEffect == null ? null : java.util.List.copyOf(varianceEffect);
        this.varianceSeverity = varianceSeverity;
        this.varianceDetection = varianceDetection;
        this.varianceRisk = varianceRisk;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFailureMode that = (StepFailureMode) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceCause, that.varianceCause) && Objects.equals(varianceEffect, that.varianceEffect) && varianceSeverity == that.varianceSeverity && varianceDetection == that.varianceDetection && varianceRisk == that.varianceRisk;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceType, varianceCause, varianceEffect, varianceSeverity, varianceDetection, varianceRisk);
    }

    @Override
    public String toString() {
        return "StepFailureMode{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceType=" + varianceType + "varianceCause=" + varianceCause + "varianceEffect=" + varianceEffect + "varianceSeverity=" + varianceSeverity + "varianceDetection=" + varianceDetection + "varianceRisk=" + varianceRisk + "}";
    }
}