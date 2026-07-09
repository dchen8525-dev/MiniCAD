package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Resolved STRESS_ANALYSIS.
 * Stress analysis type for FEA.
 */
/**
 * Resolved STRESS_ANALYSIS.
 * Stress analysis type for FEA.
 */
public final class StepStressAnalysis implements StepEntity {
    private final int id;
    private final String name;
    private final String analysisType;

    public StepStressAnalysis(int id, String name, String analysisType) {
        this.id = id;
        this.name = name;
        this.analysisType = analysisType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAnalysisType() {
        return analysisType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStressAnalysis that = (StepStressAnalysis) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(analysisType, that.analysisType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, analysisType);
    }

    @Override
    public String toString() {
        return "StepStressAnalysis{" + "id=" + id + "name=" + name + "analysisType=" + analysisType + "}";
    }
}
