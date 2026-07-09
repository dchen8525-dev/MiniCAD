package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BEST_PRACTICE.
 * A best practice entity.
 *
 * @param id STEP instance id
 * @param name practice name
 * @variancePractice practice variance description
 * @varianceArea applicable variance area
 * @varianceBenefits practice variance benefits
 * @varianceReference reference variance documentation
 * @varianceAdoption adoption variance level
 * @varianceStatus practice variance status
 */
/**
 * Resolved BEST_PRACTICE.
 * A best practice entity.
 *
 * @param id STEP instance id
 * @param name practice name
 * @variancePractice practice variance description
 * @varianceArea applicable variance area
 * @varianceBenefits practice variance benefits
 * @varianceReference reference variance documentation
 * @varianceAdoption adoption variance level
 * @varianceStatus practice variance status
 */
public final class StepBestPractice implements StepEntity {
    private final int id;
    private final String name;
    private final String variancePractice;
    private final String varianceArea;
    private final List<String> varianceBenefits;
    private final StepEntity varianceReference;
    private final int varianceAdoption;
    private final String varianceStatus;

    public StepBestPractice(int id, String name, String variancePractice, String varianceArea, List<String> varianceBenefits, StepEntity varianceReference, int varianceAdoption, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.variancePractice = variancePractice;
        this.varianceArea = varianceArea;
        this.varianceBenefits = varianceBenefits == null ? null : java.util.List.copyOf(varianceBenefits);
        this.varianceReference = varianceReference;
        this.varianceAdoption = varianceAdoption;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVariancePractice() {
        return variancePractice;
    }

    public String getVarianceArea() {
        return varianceArea;
    }

    public List<String> getVarianceBenefits() {
        return varianceBenefits;
    }

    public StepEntity getVarianceReference() {
        return varianceReference;
    }

    public int getVarianceAdoption() {
        return varianceAdoption;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBestPractice that = (StepBestPractice) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(variancePractice, that.variancePractice) && Objects.equals(varianceArea, that.varianceArea) && Objects.equals(varianceBenefits, that.varianceBenefits) && Objects.equals(varianceReference, that.varianceReference) && varianceAdoption == that.varianceAdoption && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, variancePractice, varianceArea, varianceBenefits, varianceReference, varianceAdoption, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepBestPractice{" + "id=" + id + "name=" + name + "variancePractice=" + variancePractice + "varianceArea=" + varianceArea + "varianceBenefits=" + varianceBenefits + "varianceReference=" + varianceReference + "varianceAdoption=" + varianceAdoption + "varianceStatus=" + varianceStatus + "}";
    }
}