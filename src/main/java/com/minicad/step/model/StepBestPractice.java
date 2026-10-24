package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepBestPractice extends AbstractStepEntity {
    private final String variancePractice;
    private final String varianceArea;
    private final List<String> varianceBenefits;
    private final StepEntity varianceReference;
    private final int varianceAdoption;
    private final String varianceStatus;

    public StepBestPractice(int id, String name, String variancePractice, String varianceArea, List<String> varianceBenefits, StepEntity varianceReference, int varianceAdoption, String varianceStatus) {
        super(id, name);
        this.variancePractice = variancePractice;
        this.varianceArea = varianceArea;
        this.varianceBenefits = varianceBenefits == null ? null : java.util.List.copyOf(varianceBenefits);
        this.varianceReference = varianceReference;
        this.varianceAdoption = varianceAdoption;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "variancePractice", variancePractice,
                "varianceArea", varianceArea, "varianceBenefits", varianceBenefits,
                "varianceReference", varianceReference, "varianceAdoption", varianceAdoption,
                "varianceStatus", varianceStatus);
    }
}
