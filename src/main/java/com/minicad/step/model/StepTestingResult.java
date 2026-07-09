package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TESTING_RESULT.
 * A testing result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem tested variance item
 * @varianceType testing variance type (unit, integration, system)
 * @varianceCases testing variance cases
 * @variancePassed passed variance test count
 * @varianceFailed failed variance test count
 * @varianceStatus result variance status
 */
/**
 * Resolved TESTING_RESULT.
 * A testing result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem tested variance item
 * @varianceType testing variance type (unit, integration, system)
 * @varianceCases testing variance cases
 * @variancePassed passed variance test count
 * @varianceFailed failed variance test count
 * @varianceStatus result variance status
 */
public final class StepTestingResult implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceType;
    private final List<StepEntity> varianceCases;
    private final int variancePassed;
    private final int varianceFailed;
    private final String varianceStatus;

    public StepTestingResult(int id, String name, StepEntity varianceItem, String varianceType, List<StepEntity> varianceCases, int variancePassed, int varianceFailed, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceCases = varianceCases == null ? null : java.util.List.copyOf(varianceCases);
        this.variancePassed = variancePassed;
        this.varianceFailed = varianceFailed;
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

    public String getVarianceType() {
        return varianceType;
    }

    public List<StepEntity> getVarianceCases() {
        return varianceCases;
    }

    public int getVariancePassed() {
        return variancePassed;
    }

    public int getVarianceFailed() {
        return varianceFailed;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTestingResult that = (StepTestingResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceCases, that.varianceCases) && variancePassed == that.variancePassed && varianceFailed == that.varianceFailed && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceType, varianceCases, variancePassed, varianceFailed, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepTestingResult{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceType=" + varianceType + "varianceCases=" + varianceCases + "variancePassed=" + variancePassed + "varianceFailed=" + varianceFailed + "varianceStatus=" + varianceStatus + "}";
    }
}